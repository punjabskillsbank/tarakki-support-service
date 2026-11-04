package com.tarakki.support.service;

import com.tarakki.support.dto.SupportTicketRequestDTO;
import com.tarakki.support.dto.SupportTicketResponseDTO;
import com.tarakki.support.dto.SupportTicketStatusUpdateRequestDTO;

import java.util.List;
import java.util.UUID;

public interface SupportTicketService {

    SupportTicketResponseDTO createTicket(SupportTicketRequestDTO requestDTO);
    SupportTicketResponseDTO updateTicketStatus(SupportTicketStatusUpdateRequestDTO requestDTO);

    List<SupportTicketResponseDTO> getTicketsByMember(UUID memberId);
}