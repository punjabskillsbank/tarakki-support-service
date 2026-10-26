package com.tarakki.support.service;

import com.tarakki.support.dto.SupportTicketRequestDTO;
import com.tarakki.support.dto.SupportTicketResponseDTO;
import com.tarakki.support.dto.SupportTicketStatusUpdateRequestDTO;
import com.tarakki.support.entity.SupportTicket;
import com.tarakki.support.enums.TicketStatus;
import com.tarakki.support.exception.SupportTicketNotFoundException;
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

import java.util.Optional;
import java.util.UUID;

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

    @Test
    void shouldUpdateTicketStatusSuccessfully() {
        UUID ticketId = supportTicket.getTicketId();
        SupportTicketStatusUpdateRequestDTO updateDTO =
                SupportTicketTestDataFactory.createSupportTicketStatusUpdateRequestDTO(ticketId, TicketStatus.IN_PROGRESS);

        SupportTicket updatedTicket = SupportTicketTestDataFactory.createSupportTicket(requestDTO);
        updatedTicket.setTicketId(ticketId);
        updatedTicket.setTicketStatus(TicketStatus.IN_PROGRESS);

        SupportTicketResponseDTO updatedResponseDTO =
                SupportTicketTestDataFactory.createSupportTicketResponseDTO(updatedTicket);

        when(supportTicketRepository.findById(ticketId)).thenReturn(Optional.of(supportTicket));
        when(supportTicketRepository.save(supportTicket)).thenReturn(updatedTicket);
        when(modelMapper.map(updatedTicket, SupportTicketResponseDTO.class)).thenReturn(updatedResponseDTO);

        SupportTicketResponseDTO result = supportTicketService.updateTicketStatus(updateDTO);

        assertNotNull(result);
        assertEquals(TicketStatus.IN_PROGRESS, result.getTicketStatus());
        assertEquals(ticketId, result.getTicketId());

        verify(supportTicketRepository, times(1)).findById(ticketId);
        verify(supportTicketRepository, times(1)).save(supportTicket);
        verify(modelMapper, times(1)).map(updatedTicket, SupportTicketResponseDTO.class);
    }

    @Test
    void shouldThrowSupportTicketNotFoundExceptionWhenTicketDoesNotExist() {
        UUID ticketId = UUID.randomUUID();
        SupportTicketStatusUpdateRequestDTO updateDTO =
                SupportTicketTestDataFactory.createSupportTicketStatusUpdateRequestDTO(ticketId, TicketStatus.RESOLVED);

        when(supportTicketRepository.findById(ticketId)).thenReturn(Optional.empty());

        SupportTicketNotFoundException exception = assertThrows(
                SupportTicketNotFoundException.class,
                () -> supportTicketService.updateTicketStatus(updateDTO)
        );

        assertEquals("Support ticket not found with ID: " + ticketId, exception.getMessage());
        verify(supportTicketRepository, times(1)).findById(ticketId);
        verify(supportTicketRepository, never()).save(any());
        verify(modelMapper, never()).map(any(), any());
    }
}
