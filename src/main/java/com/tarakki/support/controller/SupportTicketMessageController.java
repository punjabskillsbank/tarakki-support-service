package com.tarakki.support.controller;

import com.tarakki.support.dto.SupportTicketMessageRequestDTO;
import com.tarakki.support.dto.SupportTicketMessageResponseDTO;
import com.tarakki.support.service.SupportTicketMessageService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/support/tickets/messages")
@RequiredArgsConstructor
public class SupportTicketMessageController {

    private final SupportTicketMessageService supportTicketMessageService;

    @PostMapping
    public ResponseEntity<SupportTicketMessageResponseDTO> createMessage(
            @Valid @RequestBody SupportTicketMessageRequestDTO requestDTO) {

        SupportTicketMessageResponseDTO responseDTO = supportTicketMessageService.createMessage(requestDTO);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(responseDTO);
    }
}