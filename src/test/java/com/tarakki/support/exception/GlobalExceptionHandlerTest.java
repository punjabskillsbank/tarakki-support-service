package com.tarakki.support.exception;

import com.tarakki.support.dto.SupportTicketRequestDTO;

import jakarta.validation.Valid;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import tools.jackson.databind.ObjectMapper;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class GlobalExceptionHandlerTest {

    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(new TestController())
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    @DisplayName("POST with invalid body - should return 400 Bad Request with field errors")
    void shouldReturn400_WhenValidationFails() throws Exception {

        SupportTicketRequestDTO requestDTO = new SupportTicketRequestDTO();

        mockMvc.perform(post("/test/support-ticket")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("GET on missing resource - should return 404 Not Found with error message")
    void shouldReturn404_WhenResourceNotFound() throws Exception {

        mockMvc.perform(get("/test/not-found"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Support ticket not found with ID: abc-123"));
    }

    @RestController
    static class TestController {

        @PostMapping("/test/support-ticket")
        public void createSupportTicket(
                @Valid @RequestBody SupportTicketRequestDTO requestDTO) {
        }

        @GetMapping("/test/not-found")
        public void throwNotFound() {
            throw new SupportTicketMessageNotFoundException("Support ticket not found with ID: abc-123");
        }
    }
}
