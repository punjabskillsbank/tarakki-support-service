package com.tarakki.support.service;

import com.tarakki.support.dto.SupportTicketRequestDTO;
import com.tarakki.support.dto.SupportTicketResponseDTO;

import java.util.List;
import java.util.UUID;

public interface SupportTicketService {

    SupportTicketResponseDTO createTicket(SupportTicketRequestDTO requestDTO);
    List<SupportTicketResponseDTO> getTicketsByMember(UUID memberId);
}