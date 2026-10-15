package com.tarakki.support.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder(toBuilder = true)
@Table(name = "support_message")
public class SupportTicketMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "message_id", nullable = false, updatable = false)
    private UUID messageId;

    @Column(name = "ticket_id", nullable = false)
    private UUID ticketId;

    @Column(name = "support_id", nullable = true)
    private UUID supportId;

    @Column(name = "user_id", nullable = true)
    private UUID userId;

    @Column(name = "message_body", nullable = true, columnDefinition = "TEXT")
    private String messageBody;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

}
