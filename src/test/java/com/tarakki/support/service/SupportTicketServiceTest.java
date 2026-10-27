package com.tarakki.support.service;

import com.tarakki.support.client.MemberClient;
import com.tarakki.support.dto.SupportTicketRequestDTO;
import com.tarakki.support.dto.SupportTicketResponseDTO;
import com.tarakki.support.dto.SupportTicketStatusUpdateRequestDTO;
import com.tarakki.support.entity.SupportTicket;
import com.tarakki.support.enums.TicketStatus;
import com.tarakki.support.exception.MemberNotFoundException;
import com.tarakki.support.exception.MemberServiceUnavailableException;
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
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;

import java.util.List;
import java.util.UUID;

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

    @Mock
    private MemberClient memberClient;

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
        verify(memberClient).getMemberById(memberId);
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
        verify(memberClient).getMemberById(memberId);
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
        verify(memberClient).getMemberById(memberId);
    }

    @Test
    void shouldThrowExceptionWhenMemberDoesNotExist() {
        UUID memberId = UUID.randomUUID();

        when(memberClient.getMemberById(memberId))
                .thenThrow(HttpClientErrorException.create(
                        org.springframework.http.HttpStatus.NOT_FOUND,
                        "Not Found",
                        org.springframework.http.HttpHeaders.EMPTY,
                        new byte[0],
                        java.nio.charset.StandardCharsets.UTF_8
                ));

        assertThrows(
                MemberNotFoundException.class,
                () -> supportTicketService.getTicketsByMember(memberId)
        );

        verify(memberClient).getMemberById(memberId);
        verifyNoInteractions(supportTicketRepository);
    }
    @Test
    void getTicketsByMember_whenMemberServiceIsUnreachable_throwsUnavailableException() {
        UUID memberId = UUID.randomUUID();

        when(memberClient.getMemberById(memberId))
                .thenThrow(new ResourceAccessException("Connection refused"));

        assertThrows(
                MemberServiceUnavailableException.class,
                () -> supportTicketService.getTicketsByMember(memberId)
        );

        verify(memberClient).getMemberById(memberId);
    }
}
