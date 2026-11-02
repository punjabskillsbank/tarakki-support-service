package com.tarakki.support.service;

import com.tarakki.support.dto.SupportTicketMessageRequestDTO;
import com.tarakki.support.dto.SupportTicketMessageResponseDTO;

import java.util.List;
import java.util.UUID;

public interface SupportTicketMessageService {
    SupportTicketMessageResponseDTO createMessage(SupportTicketMessageRequestDTO requestDTO);
    List<SupportTicketMessageResponseDTO> getMessageByTicketId(UUID ticketId);

}
