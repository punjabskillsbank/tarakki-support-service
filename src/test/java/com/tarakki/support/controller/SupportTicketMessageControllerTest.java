package com.tarakki.support.controller;

import tools.jackson.databind.ObjectMapper;
import com.tarakki.support.dto.SupportTicketMessageRequestDTO;
import com.tarakki.support.dto.SupportTicketMessageResponseDTO;
import com.tarakki.support.service.SupportTicketMessageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(SupportTicketMessageController.class)
class SupportTicketMessageControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @MockitoBean
    private SupportTicketMessageService supportTicketMessageService;

    private UUID ticketId;
    private UUID messageId;
    private SupportTicketMessageRequestDTO requestDTO;
    private SupportTicketMessageResponseDTO responseDTO;

    @BeforeEach
    void setUp() {
        ticketId = UUID.randomUUID();
        messageId = UUID.randomUUID();

        requestDTO = SupportTicketMessageRequestDTO.builder()
                .ticketId(ticketId)
                .userId(UUID.randomUUID())
                .messageBody("Hello team")
                .build();

        responseDTO = SupportTicketMessageResponseDTO.builder()
                .messageId(messageId)
                .ticketId(ticketId)
                .messageBody("Hello team")
                .createdAt(LocalDateTime.now())
                .build();
    }

    @Test
    @DisplayName("POST /api/support/tickets/messages - Success 201 Created")
    void createMessage_Returns201Created() throws Exception {
        when(supportTicketMessageService.createMessage(any(SupportTicketMessageRequestDTO.class)))
                .thenReturn(responseDTO);

        mockMvc.perform(post("/api/support/tickets/messages")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.messageId").value(messageId.toString()))
                .andExpect(jsonPath("$.ticketId").value(ticketId.toString()))
                .andExpect(jsonPath("$.messageBody").value("Hello team"));
    }

    @Test
    @DisplayName("POST /api/support/tickets/messages - Validation Failure 400 Bad Request")
    void createMessage_NullTicketId_Returns400BadRequest() throws Exception {
        requestDTO.setTicketId(null);

        mockMvc.perform(post("/api/support/tickets/messages")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isBadRequest());
    }
}
