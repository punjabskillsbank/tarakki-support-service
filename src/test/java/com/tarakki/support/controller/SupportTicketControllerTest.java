package com.tarakki.support.controller;

import com.tarakki.support.dto.SupportTicketRequestDTO;
import com.tarakki.support.entity.SupportTicket;
import com.tarakki.support.service.SupportTicketService;
import com.tarakki.support.util.SupportTicketTestDataFactory;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import tools.jackson.databind.ObjectMapper;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(SupportTicketController.class)
class SupportTicketControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private SupportTicketService supportTicketService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private SupportTicketRequestDTO input;
    private SupportTicket output;

    @BeforeEach
    void setUp() {
        input = SupportTicketTestDataFactory.createSupportTicketRequestDTO();
        output = SupportTicketTestDataFactory.createSupportTicket(input);
    }

    @Test
    void shouldCreateSupportTicket() throws Exception {

        when(supportTicketService.createTicket(any()))
                .thenReturn(output);

        mockMvc.perform(post("/api/support/tickets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.ticketId").value(output.getTicketId().toString()))
                .andExpect(jsonPath("$.memberId").value(input.getMemberId().toString()))
                .andExpect(jsonPath("$.firstName").value(input.getFirstName()))
                .andExpect(jsonPath("$.lastName").value(input.getLastName()))
                .andExpect(jsonPath("$.email").value(input.getEmail()))
                .andExpect(jsonPath("$.subject").value(input.getSubject()))
                .andExpect(jsonPath("$.issueCategory").value(input.getIssueCategory().name()))
                .andExpect(jsonPath("$.issueType").value(input.getIssueType().name()))
                .andExpect(jsonPath("$.message").value(input.getMessage()))
                .andExpect(jsonPath("$.ticketStatus").value("OPEN"));
    }
}
