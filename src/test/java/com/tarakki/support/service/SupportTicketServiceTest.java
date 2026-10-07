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

import java.util.List;
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
    void shouldGetTicketsByMemberSuccessfully() {

        UUID memberId =
                SupportTicketTestDataFactory.MEMBER_ID;

        List<SupportTicket> tickets =
                List.of(supportTicket);

        when(supportTicketRepository.findByMemberId(memberId))
                .thenReturn(tickets);

        when(modelMapper.map(
                supportTicket,
                SupportTicketResponseDTO.class))
                .thenReturn(responseDTO);

        List<SupportTicketResponseDTO> result =
                supportTicketService.getTicketsByMember(memberId);

        assertNotNull(result);

        assertEquals(1, result.size());

        assertEquals(
                responseDTO.getTicketId(),
                result.get(0).getTicketId()
        );

        assertEquals(
                responseDTO.getMemberId(),
                result.get(0).getMemberId()
        );

        assertEquals(
                responseDTO.getFirstName(),
                result.get(0).getFirstName()
        );

        assertEquals(
                responseDTO.getLastName(),
                result.get(0).getLastName()
        );

        assertEquals(
                responseDTO.getEmail(),
                result.get(0).getEmail()
        );

        assertEquals(
                responseDTO.getSubject(),
                result.get(0).getSubject()
        );

        assertEquals(
                responseDTO.getIssueCategory(),
                result.get(0).getIssueCategory()
        );

        assertEquals(
                responseDTO.getIssueType(),
                result.get(0).getIssueType()
        );

        assertEquals(
                responseDTO.getMessage(),
                result.get(0).getMessage()
        );

        assertEquals(
                responseDTO.getTicketStatus(),
                result.get(0).getTicketStatus()
        );

        verify(supportTicketRepository)
                .findByMemberId(memberId);

        verify(modelMapper)
                .map(
                        supportTicket,
                        SupportTicketResponseDTO.class
                );
    }

    @Test
    void shouldReturnEmptyListWhenMemberHasNoTickets() {

        UUID memberId = UUID.randomUUID();

        when(supportTicketRepository.findByMemberId(memberId))
                .thenReturn(List.of());

        List<SupportTicketResponseDTO> result =
                supportTicketService.getTicketsByMember(memberId);

        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(supportTicketRepository)
                .findByMemberId(memberId);

        verifyNoInteractions(modelMapper);
    }

    @Test
    void shouldGetMultipleTicketsByMemberSuccessfully() {

        SupportTicket secondTicket =
                SupportTicketTestDataFactory
                        .createSupportTicket(requestDTO);

        SupportTicketResponseDTO secondResponse =
                SupportTicketTestDataFactory
                        .createSupportTicketResponseDTO(secondTicket);

        UUID memberId =
                SupportTicketTestDataFactory.MEMBER_ID;

        List<SupportTicket> tickets =
                List.of(supportTicket, secondTicket);

        when(supportTicketRepository.findByMemberId(memberId))
                .thenReturn(tickets);

        when(modelMapper.map(
                supportTicket,
                SupportTicketResponseDTO.class))
                .thenReturn(responseDTO);

        when(modelMapper.map(
                secondTicket,
                SupportTicketResponseDTO.class))
                .thenReturn(secondResponse);

        List<SupportTicketResponseDTO> result =
                supportTicketService.getTicketsByMember(memberId);

        assertNotNull(result);

        assertEquals(2, result.size());

        assertEquals(
                supportTicket.getTicketId(),
                result.get(0).getTicketId()
        );

        assertEquals(
                secondTicket.getTicketId(),
                result.get(1).getTicketId()
        );

        verify(supportTicketRepository)
                .findByMemberId(memberId);

        verify(modelMapper, times(2))
                .map(
                        any(SupportTicket.class),
                        eq(SupportTicketResponseDTO.class)
                );
    }
}
