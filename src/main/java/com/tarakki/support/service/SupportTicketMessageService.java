package com.tarakki.support.service;

import com.tarakki.support.dto.SupportTicketMessageRequestDTO;
import com.tarakki.support.dto.SupportTicketMessageResponseDTO;

public interface SupportTicketMessageService {
    SupportTicketMessageResponseDTO createMessage(SupportTicketMessageRequestDTO requestDTO);
}
