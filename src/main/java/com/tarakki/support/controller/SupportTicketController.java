package com.tarakki.support.controller;

import com.tarakki.support.dto.SupportTicketRequestDTO;
import com.tarakki.support.dto.SupportTicketResponseDTO;
import com.tarakki.support.service.SupportTicketService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/support/tickets")
@RequiredArgsConstructor
public class SupportTicketController {

    private final SupportTicketService supportTicketService;

    @PostMapping
    public ResponseEntity<SupportTicketResponseDTO> createTicket(
            @Valid @RequestBody SupportTicketRequestDTO requestDTO) {

        SupportTicketResponseDTO ticket = supportTicketService.createTicket(requestDTO);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ticket);
    }


    @GetMapping("/{memberId}")
    public ResponseEntity<List<SupportTicketResponseDTO>> getTicketsByMemberId(@PathVariable UUID memberId) {

        List<SupportTicketResponseDTO> result = supportTicketService.getTicketsByMember(memberId);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}