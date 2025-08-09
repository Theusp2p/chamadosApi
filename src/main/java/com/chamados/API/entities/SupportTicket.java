package com.chamados.API.entities;

import com.chamados.API.entities.enums.PriorityRole;
import com.chamados.API.entities.enums.SupportTicketStatusRole;
import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "support_ticket")
@Data
@EntityListeners(AuditingEntityListener.class)
public class SupportTicket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 100, nullable = false)
    private String object;

    @Column(length = 500, nullable = false)
    private String description;

    @Column(name = "comment")
    private String comment;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PriorityRole priority;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SupportTicketStatusRole status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_department")
    private Department department;

    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "start_of_service")
    private LocalDateTime startOfService;

    @Column(name = "attributed_to")
    private String attributedTo;

    @LastModifiedBy
    @Column(name = "last_modified_by")
    private String lastModifiedBy;

    @CreatedBy
    @Column(name = "created_by" , nullable = false)
    private String createdBy;




}
