package com.tarakki.support.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SupportTicketMessageResponseDTO {

    private UUID messageId;
    private UUID ticketId;
    private UUID supportId;
    private UUID userId;
    private String messageBody;
    private LocalDateTime createdAt;
}
