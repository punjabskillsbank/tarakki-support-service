package com.tarakki.support.serviceImpl;

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
import com.tarakki.support.service.SupportTicketService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SupportTicketServiceImpl implements SupportTicketService {

    private final SupportTicketRepository supportTicketRepository;
    private final ModelMapper modelMapper;
    private final MemberClient memberClient;

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

    @Override
    public SupportTicketResponseDTO updateTicketStatus(
            SupportTicketStatusUpdateRequestDTO requestDTO) {

        SupportTicket ticket = supportTicketRepository.findById(requestDTO.getTicketId())
                .orElseThrow(() -> new SupportTicketNotFoundException(requestDTO.getTicketId()));

        ticket.setTicketStatus(requestDTO.getStatus());

        SupportTicket updatedTicket = supportTicketRepository.save(ticket);

        return modelMapper.map(
                updatedTicket,
                SupportTicketResponseDTO.class
        );
    }

    @Override
    public List<SupportTicketResponseDTO> getTicketsByMember(UUID memberId) {

        try {
            memberClient.getMemberById(memberId);
        } catch (HttpClientErrorException.NotFound exception) {
            throw new MemberNotFoundException(memberId);
        } catch (RestClientException exception) {
            throw new MemberServiceUnavailableException(exception);
        }

        List<SupportTicket> tickets =
                supportTicketRepository.findByMemberId(memberId);

        return tickets.stream()
                .map(ticket ->
                        modelMapper.map(ticket, SupportTicketResponseDTO.class))
                .toList();
    }
}