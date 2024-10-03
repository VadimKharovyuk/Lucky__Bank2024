package com.example.lucky__bank.service;

import com.example.lucky__bank.Request.CreateTicketRequest;
import com.example.lucky__bank.dto.UserDTO;
import com.example.lucky__bank.model.SupportTicket;
import com.example.lucky__bank.repository.SupportTicketRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SupportTicketService {

    private final SupportTicketRepository repository;
    private final UserService userService;


    public SupportTicket createTicket(CreateTicketRequest request) {
        UserDTO user = userService.findById(request.getUserId());
        if (user == null) {
            throw new RuntimeException("User not found");
        }
        SupportTicket ticket = new SupportTicket(
                request.getUserId(),
                request.getMessage(),
                request.getTopic()
        );
        ticket.setCreatedAt(LocalDateTime.now());
        return repository.save(ticket);
    }

    public List<SupportTicket> getAllTickets() {
        return repository.findAll();
    }

    public List<SupportTicket> getUnansweredTickets() {
        return repository.findByAdminReplyIsNull();
    }

    public List<SupportTicket> getUserTickets(Long userId) {
        return repository.findByUserId(userId);
    }

    public SupportTicket replyToTicket(Long ticketId, String adminReply) {
        SupportTicket ticket = repository.findById(ticketId)
                .orElseThrow(() -> new RuntimeException("Ticket not found"));
        ticket.setAdminReply(adminReply);
        return repository.save(ticket);
    }

    public SupportTicket getticketId(Long ticketId) {
        return repository.findById(ticketId).orElseThrow();
    }
}