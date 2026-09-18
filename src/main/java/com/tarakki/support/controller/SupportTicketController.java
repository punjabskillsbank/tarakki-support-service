package com.tarakki.support.controller;

import com.tarakki.support.dto.SupportTicketRequestDTO;
import com.tarakki.support.entity.SupportTicket;
import com.tarakki.support.service.SupportTicketService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/support/tickets")
@RequiredArgsConstructor
public class SupportTicketController {

    private final SupportTicketService SupportTicketService;

    @PostMapping
    public ResponseEntity<SupportTicket> createTicket(
            @Valid @RequestBody SupportTicketRequestDTO requestDTO) {

        SupportTicket ticket = SupportTicketService.createTicket(requestDTO);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ticket);
    }
}