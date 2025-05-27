package com.chamados.API.services;

import com.chamados.API.entities.SupportTicket;
import com.chamados.API.entities.enums.PriorityRole;
import com.chamados.API.entities.enums.SupportTicketStatusRole;
import com.chamados.API.repositories.SupportTicketRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

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

    public SupportTicket update(Long id, SupportTicket ticketDetails) {
        SupportTicket ticket = findById(id);
        if (ticket != null) {
            ticket.setObject(ticketDetails.getObject());
            ticket.setDescription(ticketDetails.getDescription());
            ticket.setComment(ticketDetails.getComment());
            ticket.setPriority(ticketDetails.getPriority());
            ticket.setStatus(ticketDetails.getStatus());
            ticket.setLastModifiedBy(ticketDetails.getLastModifiedBy());
            ticket.setUpdatedAt(LocalDateTime.now());
            return repository.save(ticket);
        }
        return null;
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

    public Object countResolvedTickets() {
        return repository.countSupportTicketByStatusClose();
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

    public List<SupportTicket> filterByStatusOpen() {
        return repository.findSupportTicketByStatusOpen();
    }


    @Transactional
    public void updateTicket(Long id, SupportTicket updatedTicket) {
            var ticket = findById(id);
            if (ticket != null) {
                ticket.setPriority(updatedTicket.getPriority());
                ticket.setStatus(updatedTicket.getStatus());
                ticket.setAttributedTo(updatedTicket.getAttributedTo());
                ticket.setComment(updatedTicket.getComment());

                repository.save(ticket);
            }
    }
}