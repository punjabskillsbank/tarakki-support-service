package com.tarakki.support.dto;

import com.tarakki.support.enums.TicketStatus;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SupportTicketStatusUpdateRequestDTO {
    @NotNull(message = "Ticket ID cannot be null")
    private UUID ticketId;

    @NotNull(message = "Ticket status cannot be null")
    private TicketStatus status;
}
