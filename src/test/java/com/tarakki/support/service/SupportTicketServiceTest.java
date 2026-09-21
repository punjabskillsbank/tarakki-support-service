package com.tarakki.support.service;

import com.tarakki.support.dto.SupportTicketRequestDTO;
import com.tarakki.support.dto.SupportTicketResponseDTO;
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
import org.modelmapper.ModelMapper;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SupportTicketServiceTest {

    @Mock
    private SupportTicketRepository supportTicketRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private SupportTicketServiceImpl supportTicketService;

    private SupportTicketRequestDTO requestDTO;
    private SupportTicket supportTicket;
    private SupportTicketResponseDTO responseDTO;

    @BeforeEach
    void setUp() {
        requestDTO = SupportTicketTestDataFactory.createSupportTicketRequestDTO();
        supportTicket = SupportTicketTestDataFactory.createSupportTicket(requestDTO);
        responseDTO = SupportTicketTestDataFactory.createSupportTicketResponseDTO(supportTicket);
    }

    @Test
    void shouldCreateSupportTicket() {
        when(modelMapper.map(requestDTO, SupportTicket.class))
                .thenReturn(supportTicket);
        when(supportTicketRepository.save(any(SupportTicket.class)))
                .thenReturn(supportTicket);
        when(modelMapper.map(supportTicket, SupportTicketResponseDTO.class))
                .thenReturn(responseDTO);

        SupportTicketResponseDTO result =
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

        verify(modelMapper).map(requestDTO, SupportTicket.class);
        verify(supportTicketRepository, times(1))
                .save(any(SupportTicket.class));
        verify(modelMapper).map(supportTicket, SupportTicketResponseDTO.class);
    }
}
