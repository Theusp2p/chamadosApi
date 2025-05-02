package com.chamados.API.repositories;

import com.chamados.API.entities.SupportTicket;
import com.chamados.API.entities.enums.PriorityRole;
import com.chamados.API.entities.enums.SupportTicketStatusRole;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SupportTicketRepository extends JpaRepository<SupportTicket, Long> {

    // Chamados por prioridade e status
    List<SupportTicket> findByPriorityAndStatusNot(
            PriorityRole priority,
            SupportTicketStatusRole status,
            Pageable pageable
    );

    @Query("SELECT s FROM SupportTicket s ORDER BY s.createdAt DESC LIMIT :limit")
    List<SupportTicket> findTopNByOrderByCreatedAtDesc(@Param("limit") int limit);

    @Query("SELECT t FROM SupportTicket t WHERE t.priority = com.chamados.API.entities.enums.PriorityRole.CRITICA ORDER BY t.createdAt DESC")
    List<SupportTicket> findUrgentTickets();
}
