package com.tarakki.support.controller;

import tools.jackson.databind.ObjectMapper;
import com.tarakki.support.dto.SupportTicketMessageRequestDTO;
import com.tarakki.support.dto.SupportTicketMessageResponseDTO;
import com.tarakki.support.service.SupportTicketMessageService;
import com.tarakki.support.util.SupportTicketTestDataFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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

        requestDTO = SupportTicketTestDataFactory.createSupportTicketMessageRequestDTO(ticketId);
        responseDTO = SupportTicketTestDataFactory.createSupportTicketMessageResponseDTO(messageId, requestDTO);
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

    @Test
    @DisplayName("GET /api/support/tickets/messages/{ticket_id} - Success 200 OK")
    void getMessage_Returns200_OK() throws Exception {

        when(supportTicketMessageService.getMessageByTicketId(ticketId)).thenReturn(List.of(responseDTO));

        mockMvc.perform(get("/api/support/tickets/messages/{ticketId}",ticketId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].messageId").value(messageId.toString()))
                .andExpect(jsonPath("$[0].ticketId").value(ticketId.toString()))
                .andExpect(jsonPath("$[0].messageBody").value(responseDTO.getMessageBody()));
    }
}
