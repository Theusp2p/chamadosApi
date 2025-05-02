package com.chamados.API.services;

import com.chamados.API.entities.SupportTicket;
import com.chamados.API.entities.User;
import com.chamados.API.entities.enums.PriorityRole;
import com.chamados.API.entities.enums.SupportTicketStatusRole;
import com.chamados.API.repositories.SupportTicketRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SupportTicketService {

    private final SupportTicketRepository repository;

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

    public SupportTicket create(SupportTicket ticket, User createdBy) {
        ticket.setCreatedBy(createdBy);
        ticket.setCreatedAt(LocalDateTime.now());
        ticket.setStatus(SupportTicketStatusRole.ABERTO);
        return repository.save(ticket);
    }

    public SupportTicket update(Long id, SupportTicket ticketDetails, User modifiedBy) {
        SupportTicket ticket = findById(id);
        if (ticket != null) {
            ticket.setObject(ticketDetails.getObject());
            ticket.setDescription(ticketDetails.getDescription());
            ticket.setComment(ticketDetails.getComment());
            ticket.setPriority(ticketDetails.getPriority());
            ticket.setStatus(ticketDetails.getStatus());
            ticket.setLastModifiedBy(modifiedBy);
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

    public Object countOpenTickets() {
        return null;
    }

    public Object countResolvedTickets() {
        return null;
    }

    public Object countInProgressTickets() {
        return null;
    }

    public Object countUrgentTickets() {
        return null;
    }
}