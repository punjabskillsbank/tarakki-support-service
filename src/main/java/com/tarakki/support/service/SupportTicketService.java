package com.tarakki.support.service;

import com.tarakki.support.dto.SupportTicketRequestDTO;
import com.tarakki.support.dto.SupportTicketResponseDTO;

public interface SupportTicketService {

    SupportTicketResponseDTO createTicket(SupportTicketRequestDTO requestDTO);
}