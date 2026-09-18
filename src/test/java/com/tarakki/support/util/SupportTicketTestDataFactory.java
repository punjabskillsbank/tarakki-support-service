package com.tarakki.support.util;

import com.tarakki.support.dto.SupportTicketRequestDTO;
import com.tarakki.support.entity.IssueCategory;
import com.tarakki.support.entity.IssueType;
import com.tarakki.support.entity.SupportTicket;
import com.tarakki.support.entity.TicketStatus;

import java.time.LocalDateTime;
import java.util.UUID;

public class SupportTicketTestDataFactory {

    public static SupportTicketRequestDTO createSupportTicketRequestDTO() {

        String uniqueValue = UUID.randomUUID()
                .toString()
                .substring(0, 8);

        SupportTicketRequestDTO dto = new SupportTicketRequestDTO();

        dto.setMemberId(UUID.randomUUID());
        dto.setFirstName("User" + uniqueValue);
        dto.setLastName("Test" + uniqueValue);
        dto.setEmail("user" + uniqueValue + "@gmail.com");
        dto.setSubject("Support issue " + uniqueValue);
        dto.setIssueCategory(IssueCategory.ACCOUNT);
        dto.setIssueType(IssueType.LOGIN_ISSUE);
        dto.setMessage("Support message " + uniqueValue);

        return dto;
    }

    public static SupportTicket createSupportTicketEntity(
            SupportTicketRequestDTO dto) {

        LocalDateTime now = LocalDateTime.now();

        SupportTicket ticket = new SupportTicket();

        ticket.setTicketId(UUID.randomUUID());
        ticket.setMemberId(dto.getMemberId());
        ticket.setFirstName(dto.getFirstName());
        ticket.setLastName(dto.getLastName());
        ticket.setEmail(dto.getEmail());
        ticket.setSubject(dto.getSubject());
        ticket.setIssueCategory(dto.getIssueCategory());
        ticket.setIssueType(dto.getIssueType());
        ticket.setMessage(dto.getMessage());
        ticket.setTicketStatus(TicketStatus.OPEN);
        ticket.setCreatedAt(now);
        ticket.setUpdatedAt(now);

        return ticket;
    }

    public static SupportTicket createSupportTicket(SupportTicketRequestDTO dto) {
        return createSupportTicketEntity(dto);
    }
}
