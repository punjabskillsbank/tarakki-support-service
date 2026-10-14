package com.tarakki.support.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SupportTicketMessageRequestDTO {
    @NotNull(message = "Ticket ID is required")
    private UUID ticketId;

    private UUID supportId;

    private UUID userId;

    private String messageBody;
}
