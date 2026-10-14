package com.tarakki.support.service;

import com.tarakki.support.dto.SupportTicketMessageRequestDTO;
import com.tarakki.support.dto.SupportTicketMessageResponseDTO;
import com.tarakki.support.entity.SupportTicketMessage;
import com.tarakki.support.repository.SupportTicketMessageRepository;
import com.tarakki.support.repository.SupportTicketRepository;
import com.tarakki.support.serviceImpl.SupportTicketMessageServiceImpl;
import com.tarakki.support.exception.SupportTicketMessageNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SupportTicketMessageServiceTest {

    @Mock
    private SupportTicketMessageRepository supportTicketMessageRepository;

    @Mock
    private SupportTicketRepository supportTicketRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private SupportTicketMessageServiceImpl supportTicketMessageService;

    private UUID ticketId;
    private UUID userId;
    private UUID supportId;
    private UUID messageId;
    private SupportTicketMessageRequestDTO requestDTO;
    private SupportTicketMessage messageEntity;
    private SupportTicketMessageResponseDTO responseDTO;

    @BeforeEach
    void setUp() {
        ticketId = UUID.randomUUID();
        userId = UUID.randomUUID();
        supportId = UUID.randomUUID();
        messageId = UUID.randomUUID();

        requestDTO = SupportTicketMessageRequestDTO.builder()
                .ticketId(ticketId)
                .userId(userId)
                .supportId(supportId)
                .messageBody("Test message body")
                .build();

        messageEntity = SupportTicketMessage.builder()
                .messageId(messageId)
                .ticketId(ticketId)
                .userId(userId)
                .supportId(supportId)
                .messageBody("Test message body")
                .createdAt(LocalDateTime.now())
                .build();

        responseDTO = SupportTicketMessageResponseDTO.builder()
                .messageId(messageId)
                .ticketId(ticketId)
                .userId(userId)
                .supportId(supportId)
                .messageBody("Test message body")
                .createdAt(messageEntity.getCreatedAt())
                .build();
    }

    @Test
    @DisplayName("Create Message - Success Scenario")
    void createMessage_Success() {

        when(supportTicketRepository.existsById(ticketId)).thenReturn(true);
        when(modelMapper.map(requestDTO, SupportTicketMessage.class)).thenReturn(messageEntity);
        when(supportTicketMessageRepository.save(any(SupportTicketMessage.class))).thenReturn(messageEntity);
        when(modelMapper.map(messageEntity, SupportTicketMessageResponseDTO.class)).thenReturn(responseDTO);

        SupportTicketMessageResponseDTO result = supportTicketMessageService.createMessage(requestDTO);

        assertNotNull(result);
        assertEquals(messageId, result.getMessageId());
        assertEquals(ticketId, result.getTicketId());
        assertEquals("Test message body", result.getMessageBody());

        verify(supportTicketRepository, times(1)).existsById(ticketId);
        verify(supportTicketMessageRepository, times(1)).save(messageEntity);
    }

    @Test
    @DisplayName("Create Message - Ticket Not Found Exception")
    void createMessage_TicketNotFound_ThrowsException() {

        when(supportTicketRepository.existsById(ticketId)).thenReturn(false);

        SupportTicketMessageNotFoundException exception = assertThrows(
                SupportTicketMessageNotFoundException.class,
                () -> supportTicketMessageService.createMessage(requestDTO)
        );

        assertEquals("Support ticket not found with ID: " + ticketId, exception.getMessage());
        verify(supportTicketRepository, times(1)).existsById(ticketId);
        verify(supportTicketMessageRepository, never()).save(any());
    }
}
