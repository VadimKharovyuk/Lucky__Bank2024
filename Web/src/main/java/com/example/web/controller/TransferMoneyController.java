package com.example.web.controller;

import com.example.web.Request.MoneyTransferRequest;
import com.example.web.dto.CardDTO;
import com.example.web.dto.TransactionDto;
import com.example.web.dto.UserDTO;
import com.example.web.service.CardService;
import com.example.web.service.MoneyTransferService;
import com.example.web.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@Slf4j
@RequiredArgsConstructor
public class TransferMoneyController {
    private final MoneyTransferService moneyTransferService ;
    private final CardService cardService ;
    private final UserService userService;

    @GetMapping("/transfer")
    public String showTransferForm(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        UserDTO user = null; // Инициализация переменной

        if (authentication.getPrincipal() instanceof OAuth2User) {
            OAuth2User oauthUser = (OAuth2User) authentication.getPrincipal();
            String username = oauthUser.getAttribute("name");
            String email = oauthUser.getAttribute("email");

            // Поищите пользователя в базе данных по email или username
            user = userService.findByEmail(email); // Убедитесь, что у вас есть метод для поиска по email
        } else {
            String username = authentication.getName();
            user = userService.findByUsername(username); // Получаем пользователя из БД
        }
        List<CardDTO> cardUsers = cardService.getCardsByUserId(user.getId());
        model.addAttribute("userCards", cardUsers);


        // Проверьте, что объект user не null
        if (user != null) {
            model.addAttribute("user", user); // Добавьте пользователя в модель
        } else {
            // Обработка случая, когда пользователь не найден
            model.addAttribute("error", "User not found");
        }


        model.addAttribute("moneyTransferRequest", new MoneyTransferRequest());

        return "user/transfer/transfer";
    }

    @PostMapping("/transfer")
    public String processTransfer(MoneyTransferRequest moneyTransferRequest,
                                  RedirectAttributes redirectAttributes) {
        try {
            TransactionDto transactionDto = moneyTransferService.sendMoney(moneyTransferRequest);
            redirectAttributes.addFlashAttribute("successMessage",
                    "Money successfully transferred to card: " + transactionDto.getToCardNumber());
            return "redirect:/transfer"; // Перенаправление после успешного перевода
        } catch (Exception e) {
            log.error("Error transferring money: ", e);
            redirectAttributes.addFlashAttribute("errorMessage",
                    "Error transferring money: " + e.getMessage());
            return "redirect:/transfer";
        }
    }



}
