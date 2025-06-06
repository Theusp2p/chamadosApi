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

    @Query("SELECT t FROM SupportTicket t ORDER BY t.createdAt DESC LIMIT :limit")
    List<SupportTicket> findTopNByOrderByCreatedAtDesc(@Param("limit") int limit);

    @Query("SELECT t FROM SupportTicket t WHERE t.priority = com.chamados.API.entities.enums.PriorityRole.CRITICA ORDER BY t.createdAt DESC")
    List<SupportTicket> findUrgentTickets();

    @Query("SELECT t FROM SupportTicket t WHERE t.status = 'EM_ANDAMENTO' ORDER BY t.createdAt DESC")
    List<SupportTicket> findInProgressTickets();

    @Query("SELECT t FROM SupportTicket t WHERE t.status = 'ABERTO' ORDER BY t.createdAt DESC")
    List<SupportTicket> findSupportTicketByStatusOpen();

    @Query("SELECT COUNT(t) FROM SupportTicket t WHERE t.status = 'ABERTO'")
    Long countSupportTicketByStatusOpen();

    @Query("SELECT COUNT(t) FROM SupportTicket t WHERE t.status = 'FECHADO'")
    Long countSupportTicketByStatusClose();

    @Query("SELECT COUNT(t) FROM SupportTicket t WHERE t.status = 'EM_ANDAMENTO'")
    Long countSupportTicketByStatusInProgress();

    @Query("SELECT COUNT(t) FROM SupportTicket t WHERE t.priority = 'CRITICA'")
    Long countSupportTicketByPriorityUrgent();



}
