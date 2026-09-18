package com.tarakki.support.service;

import com.tarakki.support.dto.SupportTicketRequestDTO;
import com.tarakki.support.entity.SupportTicket;
import com.tarakki.support.repository.SupportTicketRepository;
import com.tarakki.support.serviceImpl.SupportTicketServiceImpl;
import com.tarakki.support.util.SupportTicketTestDataFactory;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SupportTicketServiceTest {

    @Mock
    private SupportTicketRepository supportTicketRepository;

    @InjectMocks
    private SupportTicketServiceImpl supportTicketService;

    private SupportTicketRequestDTO requestDTO;
    private SupportTicket supportTicket;

    @BeforeEach
    void setUp() {

        requestDTO = SupportTicketTestDataFactory.createSupportTicketRequestDTO();

        supportTicket = SupportTicketTestDataFactory.createSupportTicket(requestDTO);
    }

    @Test
    void shouldCreateSupportTicket() {

        when(supportTicketRepository.save(any(SupportTicket.class)))
                .thenReturn(supportTicket);

        SupportTicket result =
                supportTicketService.createTicket(requestDTO);

        assertNotNull(result);

        assertEquals(
                requestDTO.getMemberId(),
                result.getMemberId()
        );

        assertEquals(
                requestDTO.getFirstName(),
                result.getFirstName()
        );

        assertEquals(
                requestDTO.getLastName(),
                result.getLastName()
        );

        assertEquals(
                requestDTO.getEmail(),
                result.getEmail()
        );

        assertEquals(
                requestDTO.getSubject(),
                result.getSubject()
        );

        assertEquals(
                requestDTO.getIssueCategory(),
                result.getIssueCategory()
        );

        assertEquals(
                requestDTO.getIssueType(),
                result.getIssueType()
        );

        assertEquals(
                requestDTO.getMessage(),
                result.getMessage()
        );

        verify(supportTicketRepository, times(1))
                .save(any(SupportTicket.class));
    }

    @Test
    void shouldPropagateExceptionWhenRepositoryFails() {

        when(supportTicketRepository.save(any(SupportTicket.class)))
                .thenThrow(new RuntimeException("Database error"));

        assertThrows(
                RuntimeException.class,
                () -> supportTicketService.createTicket(requestDTO)
        );

        verify(supportTicketRepository, times(1))
                .save(any(SupportTicket.class));
    }
}
