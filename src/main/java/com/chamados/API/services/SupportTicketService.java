package com.chamados.API.services;

import com.chamados.API.entities.SupportTicket;
import com.chamados.API.entities.TicketAttachment;
import com.chamados.API.entities.User;
import com.chamados.API.entities.enums.PriorityRole;
import com.chamados.API.entities.enums.SupportTicketStatusRole;
import com.chamados.API.exceptions.SupportTicketNotFoundException;
import com.chamados.API.repositories.SupportTicketRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SupportTicketService {

    private final SupportTicketRepository repository;
    private final UserService userService;

        // Método para chamados urgentes
        public List<SupportTicket> findByPriorityAndStatusNot(
                PriorityRole priority,
                SupportTicketStatusRole status,
                Pageable pageable
        ) {
            return repository.findByPriorityAndStatusNot(priority, status, pageable);
        }

        // Método para últimos chamados
        public List<SupportTicket> findLatestTickets(int limit) {
            return repository.findTopNByOrderByCreatedAtDesc(limit);
        }

    public List<SupportTicket> findAll() {
        return repository.findAll();
    }

    public SupportTicket findById(Long id) {
        return repository.findById(id).orElse(null);
    }

    public SupportTicket create(SupportTicket ticket) {
        ticket.setCreatedBy(ticket.getCreatedBy());
        ticket.setCreatedAt(LocalDateTime.now());
        ticket.setStatus(SupportTicketStatusRole.ABERTO);
        ticket.setPriority(PriorityRole.MEDIA);
        return repository.save(ticket);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }

    public Object findUrgentTickets() {
        return repository.findUrgentTickets();
    }

    public Object findInProgressTickets() {
            return repository.findInProgressTickets();
    }

    public Object countOpenTickets() {
        return repository.countSupportTicketByStatusOpen();
    }

    public Object countInProgressTickets() {
        return repository.countSupportTicketByStatusInProgress();
    }

    public Object countUrgentTickets() {
        return repository.countSupportTicketByPriorityUrgent();
    }

    public Object filterByStatus(String status) {
        return null;
    }

    public Page<SupportTicket> findOpenTicketsPaginated(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        return repository.findOpenTicketsPaginated(pageable);
    }

    public SupportTicket findLatestTicket() {
        return repository.findTopByOrderByIdDesc();
    }


    @Transactional
    public void updateTicket(Long id, SupportTicket updatedTicket) {
            var ticket = findById(id);
        if (ticket != null) {
            ticket.setObject(ticket.getObject());
            ticket.setDescription(ticket.getDescription());
            ticket.setComment(updatedTicket.getComment());
            ticket.setAttributedTo(updatedTicket.getAttributedTo());
            ticket.setPriority(updatedTicket.getPriority());
            ticket.setStatus(updatedTicket.getStatus());
            if(updatedTicket.getStatus() == SupportTicketStatusRole.EM_ANDAMENTO) {
                ticket.setStartOfService(LocalDateTime.now());
            }
            ticket.setLastModifiedBy(updatedTicket.getLastModifiedBy());
            ticket.setUpdatedAt(LocalDateTime.now());
            repository.save(ticket);
        } else{
            throw new SupportTicketNotFoundException("Ticket not found");
        }
    }

    public List<SupportTicket> findCloseTicketsByDate(LocalDateTime startOfDay, LocalDateTime endOfDay) {
        return repository.findCloseTicketsByDate(startOfDay, endOfDay);
    }

    public Object countResolvedTickets(LocalDateTime startOfDay, LocalDateTime endOfDay) {
        return repository.countSupportTicketByStatusClose(startOfDay, endOfDay);
    }


    public Page<SupportTicket> getMyTickets(int page, int size, Authentication authentication) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        String username = authentication.getName();
        return repository.findByCreatedBy(username, pageable);
    }
}