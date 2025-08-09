package com.chamados.API.entities;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Entity
@Table(name = "ticket_attachments")
@Data
public class TicketAttachment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String mimeType; // "image/png", "image/jpeg" (formatos comuns de screenshot)

    @Column(nullable = false)
    private String storageKey; // Identificador único no storage (S3, filesystem, etc)

    @Column(nullable = false)
    private Integer fileSize; // Tamanho em bytes (para controle)

    @Column(nullable = false)
    private Boolean isScreenshot = true; // Flag específica para prints

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ticket_id", nullable = false)
    private SupportTicket supportTicket;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;
}
