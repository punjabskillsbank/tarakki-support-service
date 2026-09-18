package com.tarakki.support.service;

import com.tarakki.support.dto.SupportTicketRequestDTO;
import com.tarakki.support.entity.SupportTicket;

public interface SupportTicketService {

    SupportTicket createTicket(SupportTicketRequestDTO requestDTO);
}