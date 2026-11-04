package com.tarakki.support.controller;

import com.tarakki.support.dto.SupportTicketRequestDTO;
import com.tarakki.support.dto.SupportTicketResponseDTO;
import com.tarakki.support.dto.SupportTicketStatusUpdateRequestDTO;
import com.tarakki.support.entity.SupportTicket;
import com.tarakki.support.enums.TicketStatus;
import com.tarakki.support.exception.MemberNotFoundException;
import com.tarakki.support.service.SupportTicketService;
import com.tarakki.support.util.SupportTicketTestDataFactory;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import tools.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
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
    private SupportTicketResponseDTO output;

    @BeforeEach
    void setUp() {
        input = SupportTicketTestDataFactory.createSupportTicketRequestDTO();
        SupportTicket ticket = SupportTicketTestDataFactory.createSupportTicket(input);
        output = SupportTicketTestDataFactory.createSupportTicketResponseDTO(ticket);
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
    @Test
    @DisplayName("PATCH /api/support/tickets/status - Success 200 OK")
    void shouldUpdateTicketStatusSuccessfully() throws Exception {
        UUID ticketId = output.getTicketId();
        SupportTicketStatusUpdateRequestDTO statusRequestDTO =
                SupportTicketTestDataFactory.createSupportTicketStatusUpdateRequestDTO(ticketId, TicketStatus.IN_PROGRESS);

        SupportTicketResponseDTO updatedOutput = SupportTicketResponseDTO.builder()
                .ticketId(ticketId)
                .memberId(output.getMemberId())
                .firstName(output.getFirstName())
                .lastName(output.getLastName())
                .email(output.getEmail())
                .subject(output.getSubject())
                .issueCategory(output.getIssueCategory())
                .issueType(output.getIssueType())
                .message(output.getMessage())
                .ticketStatus(TicketStatus.IN_PROGRESS)
                .createdAt(output.getCreatedAt())
                .updatedAt(output.getUpdatedAt())
                .build();

        when(supportTicketService.updateTicketStatus(any(SupportTicketStatusUpdateRequestDTO.class)))
                .thenReturn(updatedOutput);

        mockMvc.perform(patch("/api/support/tickets/status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(statusRequestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.ticketId").value(ticketId.toString()))
                .andExpect(jsonPath("$.ticketStatus").value("IN_PROGRESS"));
    }

    @Test
    @DisplayName("PATCH /api/support/tickets/status - Null Ticket ID returns 400 Bad Request")
    void shouldReturn400WhenTicketStatusUpdateRequestHasNullTicketId() throws Exception {
        SupportTicketStatusUpdateRequestDTO invalidRequest =
                SupportTicketTestDataFactory.createSupportTicketStatusUpdateRequestDTO(null, TicketStatus.RESOLVED);

        mockMvc.perform(patch("/api/support/tickets/status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("PATCH /api/support/tickets/status - Null Status returns 400 Bad Request")
    void shouldReturn400WhenTicketStatusUpdateRequestHasNullStatus() throws Exception {
        SupportTicketStatusUpdateRequestDTO invalidRequest =
                SupportTicketTestDataFactory.createSupportTicketStatusUpdateRequestDTO(UUID.randomUUID(), null);

        mockMvc.perform(patch("/api/support/tickets/status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldGetTicketsByMemberAndReturn200Ok() throws Exception {

        UUID memberId =
                SupportTicketTestDataFactory.MEMBER_ID;

        List<SupportTicketResponseDTO> tickets =
                List.of(output);

        when(supportTicketService.getTicketsByMember(memberId))
                .thenReturn(tickets);

        mockMvc.perform(
                        get("/api/support/tickets/{memberId}", memberId)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].ticketId")
                        .value(output.getTicketId().toString()))
                .andExpect(jsonPath("$[0].memberId")
                        .value(output.getMemberId().toString()))
                .andExpect(jsonPath("$[0].firstName")
                        .value(output.getFirstName()))
                .andExpect(jsonPath("$[0].lastName")
                        .value(output.getLastName()))
                .andExpect(jsonPath("$[0].email")
                        .value(output.getEmail()))
                .andExpect(jsonPath("$[0].subject")
                        .value(output.getSubject()))
                .andExpect(jsonPath("$[0].issueCategory")
                        .value(output.getIssueCategory().name()))
                .andExpect(jsonPath("$[0].issueType")
                        .value(output.getIssueType().name()))
                .andExpect(jsonPath("$[0].message")
                        .value(output.getMessage()))
                .andExpect(jsonPath("$[0].ticketStatus")
                        .value(output.getTicketStatus().name()));

        verify(supportTicketService)
                .getTicketsByMember(memberId);
    }

    @Test
    void shouldReturnEmptyListWhenMemberHasNoTickets() throws Exception {

        UUID memberId = UUID.randomUUID();

        when(supportTicketService.getTicketsByMember(memberId))
                .thenReturn(List.of());

        mockMvc.perform(
                        get("/api/support/tickets/{memberId}", memberId)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(0));

        verify(supportTicketService)
                .getTicketsByMember(memberId);
    }

    @Test
    void shouldReturn404WhenMemberDoesNotExist() throws Exception {
        UUID memberId = UUID.randomUUID();

        when(supportTicketService.getTicketsByMember(memberId))
                .thenThrow(new MemberNotFoundException(memberId));

        mockMvc.perform(
                        get("/api/support/tickets/{memberId}", memberId)
                )
                .andExpect(status().isNotFound())
                .andExpect(jsonPath(
                "$.error")
                .value("Member not found with id: " + memberId));

        verify(supportTicketService).getTicketsByMember(memberId);
    }
}
