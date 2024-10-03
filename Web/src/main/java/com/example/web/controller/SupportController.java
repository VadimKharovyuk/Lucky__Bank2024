package com.example.web.controller;

import com.example.web.Request.CreateTicketRequest;
import com.example.web.dto.SupportTicketDto;
import com.example.web.dto.UserDTO;
import com.example.web.service.SupportService;
import com.example.web.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/support")
public class SupportController {
    private final UserService userService;
    private final SupportService supportService;



    //форма для заполнения
    @GetMapping
    public String supportForm(Model model) {
        UserDTO currentUser = getCurrentUser(SecurityContextHolder.getContext().getAuthentication());
        model.addAttribute("supportForm", new SupportTicketDto());
        model.addAttribute("user", currentUser);
        return "user/Support/Create";
    }
 // отправка заявки
    @PostMapping("/create")
    public String createTicket(@ModelAttribute SupportTicketDto supportTicketDto, Model model) {
        UserDTO currentUser = getCurrentUser(SecurityContextHolder.getContext().getAuthentication());
        if (currentUser != null) {
            supportTicketDto.setUserId(currentUser.getId());
        } else {
            model.addAttribute("error", "Пользователь не найден.");
            return "user/Support/Create";
        }

        CreateTicketRequest createTicketRequest = new CreateTicketRequest(
                supportTicketDto.getUserId(),
                supportTicketDto.getMessage(),
                supportTicketDto.getTopic()
        );

        try {
            supportService.create(createTicketRequest);
        } catch (Exception e) {
            model.addAttribute("supportForm", new SupportTicketDto());
            model.addAttribute("error", "Ошибка при создании тикета: " + e.getMessage());
            return "user/Support/Create";
        }

        return "redirect:/support/my-tickets";
    }





//список тикетов  юзера
@GetMapping("/my-tickets")
public String getUserTickets(Model model) {
    UserDTO currentUser = getCurrentUser(SecurityContextHolder.getContext().getAuthentication());
    if (currentUser != null) {
        List<SupportTicketDto> tickets = supportService.getUserTickets(currentUser.getId());
        model.addAttribute("tickets", tickets);
        return "user/Support/MyTickets";
    } else {
        return "redirect:/login";
    }
}
//Неотвеченные тикеты

    @GetMapping("/unanswered")
    public String getUnansweredTickets(Model model) {

        // Вызываем метод, который получает тикеты с именами пользователей, создавших тикеты
        List<SupportTicketDto> unansweredTickets = supportService.getNameTicket();
        UserDTO currentUser = getCurrentUser(SecurityContextHolder.getContext().getAuthentication());

        // Добавляем список тикетов и текущего пользователя в модель
        model.addAttribute("tickets", unansweredTickets);
        model.addAttribute("user", currentUser);

        return "user/Support/UnansweredTickets";
    }

    //ответ на тикет
    @PostMapping("/reply")
    public String replyToTicket(@RequestParam Long ticketId, @RequestParam String adminReply, Model model) {
        try {
            SupportTicketDto updatedTicket = supportService.replyToTicket(ticketId, adminReply);
            model.addAttribute("message", "Ответ успешно отправлен");
            model.addAttribute("ticket", updatedTicket);
        } catch (Exception e) {
            model.addAttribute("error", "Ошибка при ответе на тикет: " + e.getMessage());
        }
        return "redirect:/support/unanswered";
    }

    @GetMapping("/ticket/{ticketId}")
    public String showTicketDetails(@PathVariable Long ticketId, Model model) {
        SupportTicketDto ticket = supportService.getTicketById(ticketId);
        if (ticket != null) {
            System.out.println("User Name: " + ticket.getUserName()); // теперь это должно выводить имя
            model.addAttribute("ticket", ticket);
            // остальной код
            return "user/Support/TicketDetails";
        } else {
            return "redirect:/support/unanswered";
        }
    }

    //Детали тикета
//    @GetMapping("/ticket/{ticketId}")
//    public String showTicketDetails(@PathVariable Long ticketId, Model model) {
//
//        SupportTicketDto ticket = supportService.getTicketById(ticketId);
//        System.out.println("User Name: " + ticket.getUserName());
//        if (ticket != null) {
//            model.addAttribute("ticket", ticket);
//            return "user/Support/TicketDetails";
//        } else {
//            return "redirect:/support/unanswered";
//        }
//    }




    private UserDTO getCurrentUser(Authentication authentication) {
        if (authentication == null) {
            return null;
        }

        if (authentication.getPrincipal() instanceof OAuth2User) {
            OAuth2User oauthUser = (OAuth2User) authentication.getPrincipal();
            String email = oauthUser.getAttribute("email");
            return userService.findByEmail(email);
        } else {
            String username = authentication.getName();
            return userService.findByUsername(username);
        }
    }
}