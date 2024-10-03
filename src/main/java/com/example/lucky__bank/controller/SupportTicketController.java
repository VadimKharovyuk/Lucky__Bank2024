package com.example.lucky__bank.controller;

import com.example.lucky__bank.Request.CreateTicketRequest;
import com.example.lucky__bank.model.SupportTicket;
import com.example.lucky__bank.service.SupportTicketService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/support")
@RequiredArgsConstructor
public class SupportTicketController {

    private  final SupportTicketService service;
//создать сапорт
    @PostMapping("/tickets")
    public ResponseEntity<SupportTicket> createTicket(@Valid @RequestBody CreateTicketRequest request) {
        SupportTicket ticket = service.createTicket(request);
        return ResponseEntity.ok(ticket);
    }
//юзера  запросы
    @GetMapping("/tickets/user/{userId}")
    public ResponseEntity<List<SupportTicket>> getUserTickets(@PathVariable Long userId) {
        return ResponseEntity.ok(service.getUserTickets(userId));
    }

    @GetMapping("/tickets/unanswered")
    public ResponseEntity<List<SupportTicket>> getUnansweredTickets() {
        return ResponseEntity.ok(service.getUnansweredTickets());
    }
//ответы
    @PostMapping("/tickets/{ticketId}/reply")
    public ResponseEntity<SupportTicket> replyToTicket(@PathVariable Long ticketId, @RequestParam String adminReply) {
        return ResponseEntity.ok(service.replyToTicket(ticketId, adminReply));
    }

    @GetMapping("/tickets/{ticketId}")
    public ResponseEntity<SupportTicket> getticketId(@PathVariable Long ticketId ){
       return ResponseEntity.ok(service.getticketId(ticketId));
    }

    @GetMapping("/tickets/list")
    public ResponseEntity<List<SupportTicket>> list() {
        List<SupportTicket> list = service.getAllTickets();
        return ResponseEntity.ok(list);
    }
}
