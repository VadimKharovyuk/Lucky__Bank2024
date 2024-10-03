package com.example.web.repository;

import com.example.web.Request.CreateTicketRequest;
import com.example.web.dto.SupportTicketDto;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "SupportService", url = "http://localhost:1000")
public interface SupportServiceClient {
    @PostMapping("/api/support/tickets")
    SupportTicketDto createTicket(@RequestBody CreateTicketRequest createTicketRequest);

    @GetMapping("/api/support/tickets/user/{userId}")
    List<SupportTicketDto> getUserTickets(@PathVariable Long userId);

    @GetMapping("/api/support/tickets/unanswered")
    List<SupportTicketDto> getUnansweredTickets();

    @PostMapping("/api/support/tickets/{ticketId}/reply")
    SupportTicketDto replyToTicket(@PathVariable Long ticketId, @RequestParam String adminReply);

    @GetMapping("/api/support/tickets/{ticketId}")
    SupportTicketDto getTicketById(@PathVariable Long ticketId);


    @GetMapping("/api/support/tickets/list")
    List<SupportTicketDto> tickesList ();

}


