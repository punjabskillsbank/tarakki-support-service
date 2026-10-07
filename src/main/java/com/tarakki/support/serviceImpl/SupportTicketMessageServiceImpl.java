package com.tarakki.support.serviceImpl;

import com.tarakki.support.dto.SupportTicketMessageRequestDTO;
import com.tarakki.support.dto.SupportTicketMessageResponseDTO;
import com.tarakki.support.entity.SupportTicketMessage;
import com.tarakki.support.repository.SupportTicketMessageRepository;
import com.tarakki.support.repository.SupportTicketRepository;
import com.tarakki.support.service.SupportTicketMessageService;
import com.tarakki.support.exception.SupportTicketMessageNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SupportTicketMessageServiceImpl implements SupportTicketMessageService {

    private final SupportTicketMessageRepository supportTicketMessageRepository;
    private final SupportTicketRepository supportTicketRepository;
    private final ModelMapper modelMapper;

    @Override
    public SupportTicketMessageResponseDTO createMessage(SupportTicketMessageRequestDTO requestDTO) {

        if (!supportTicketRepository.existsById(requestDTO.getTicketId())) {
            throw new SupportTicketMessageNotFoundException("Support ticket not found with ID: " + requestDTO.getTicketId());
        }

        SupportTicketMessage ticketMessage = modelMapper.map(requestDTO, SupportTicketMessage.class);

        SupportTicketMessage savedMessage = supportTicketMessageRepository.save(ticketMessage);

        return modelMapper.map(savedMessage, SupportTicketMessageResponseDTO.class);
    }
}