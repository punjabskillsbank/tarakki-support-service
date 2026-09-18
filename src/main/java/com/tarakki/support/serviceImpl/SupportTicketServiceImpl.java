package com.tarakki.support.serviceImpl;

import com.tarakki.support.dto.SupportTicketRequestDTO;
import com.tarakki.support.entity.SupportTicket;
import com.tarakki.support.entity.TicketStatus;
import com.tarakki.support.repository.SupportTicketRepository;
import com.tarakki.support.service.SupportTicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class SupportTicketServiceImpl implements SupportTicketService {

    private final SupportTicketRepository supportTicketRepository;

    @Override
    public SupportTicket createTicket(SupportTicketRequestDTO requestDTO) {

        LocalDateTime now = LocalDateTime.now();

        SupportTicket ticket = SupportTicket.builder()
                .memberId(requestDTO.getMemberId())
                .firstName(requestDTO.getFirstName())
                .lastName(requestDTO.getLastName())
                .email(requestDTO.getEmail())
                .subject(requestDTO.getSubject())
                .issueCategory(requestDTO.getIssueCategory())
                .issueType(requestDTO.getIssueType())
                .message(requestDTO.getMessage())
                .ticketStatus(TicketStatus.OPEN)
                .createdAt(now)
                .updatedAt(now)
                .build();

        return supportTicketRepository.save(ticket);
    }
}