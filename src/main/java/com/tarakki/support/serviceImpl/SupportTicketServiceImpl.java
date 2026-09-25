package com.tarakki.support.serviceImpl;

import com.tarakki.support.dto.SupportTicketRequestDTO;
import com.tarakki.support.dto.SupportTicketResponseDTO;
import com.tarakki.support.entity.SupportTicket;
import com.tarakki.support.enums.TicketStatus;
import com.tarakki.support.repository.SupportTicketRepository;
import com.tarakki.support.service.SupportTicketService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SupportTicketServiceImpl implements SupportTicketService {

    private final SupportTicketRepository supportTicketRepository;
    private final ModelMapper modelMapper;

    @Override
    public SupportTicketResponseDTO createTicket(
            SupportTicketRequestDTO requestDTO) {

        SupportTicket ticket =
                modelMapper.map(requestDTO, SupportTicket.class);

        ticket.setTicketStatus(TicketStatus.OPEN);

        SupportTicket savedTicket =
                supportTicketRepository.save(ticket);

        return modelMapper.map(
                savedTicket,
                SupportTicketResponseDTO.class
        );
    }
}