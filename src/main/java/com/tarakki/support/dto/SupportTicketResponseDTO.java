package com.tarakki.support.dto;

import com.tarakki.support.enums.IssueCategory;
import com.tarakki.support.enums.IssueType;
import com.tarakki.support.enums.TicketStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SupportTicketResponseDTO {

    private UUID ticketId;

    private UUID memberId;

    private String firstName;

    private String lastName;

    private String email;

    private String subject;

    private IssueCategory issueCategory;

    private IssueType issueType;

    private String message;

    private TicketStatus ticketStatus;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}