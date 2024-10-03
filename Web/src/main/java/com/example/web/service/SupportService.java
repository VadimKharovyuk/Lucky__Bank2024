package com.example.web.service;

import com.example.web.Request.CreateTicketRequest;
import com.example.web.dto.SupportTicketDto;
import com.example.web.dto.UserDTO;
import com.example.web.repository.SupportServiceClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SupportService {

    private final SupportServiceClient supportServiceClient ;
    private final UserService userService ;

    public SupportTicketDto create (CreateTicketRequest createTicketRequest){
       return supportServiceClient.createTicket(createTicketRequest);
    }

    public List<SupportTicketDto> getUserTickets(Long userId){
        return  supportServiceClient.getUserTickets(userId);
    }
    public List<SupportTicketDto>getUnansweredTickets (){
        return supportServiceClient.getUnansweredTickets();
    }
    public SupportTicketDto replyToTicket(Long ticketId ,String adminReply){
      return   supportServiceClient.replyToTicket(ticketId,adminReply);
    }

//    public SupportTicketDto getTicketById(Long ticketId) {
//       return supportServiceClient.getTicketById(ticketId);
//    }


    public List<SupportTicketDto> getNameTicket() {
        List<SupportTicketDto> tickets = supportServiceClient.getUnansweredTickets();

        // Для каждого тикета получаем пользователя, который отправил этот тикет
        tickets.forEach(ticket -> {
            UserDTO user = userService.findById(ticket.getUserId());
            // Устанавливаем имя пользователя, отправившего тикет
            ticket.setUserName(user.getUsername());
        });

        return tickets;
    }
    public SupportTicketDto getTicketById(Long ticketId) {
        SupportTicketDto ticket = supportServiceClient.getTicketById(ticketId);
        if (ticket != null) {
            UserDTO user = userService.findById(ticket.getUserId());
            ticket.setUserName(user.getUsername());
        }
        return ticket;
    }

}
