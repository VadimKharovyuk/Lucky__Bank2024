package com.example.web.controller;
import com.example.web.dto.CardDTO;
import com.example.web.dto.DepositDto;
import com.example.web.dto.UserDTO;
import com.example.web.service.CardService;
import com.example.web.service.DepositService;
import com.example.web.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/deposits")
@Slf4j
public class DepositController {
    private final DepositService depositService;
    private final UserService userService;
    private final CardService cardService;

    @GetMapping("/create-form")
    public String showCreateDepositForm(Model model) {
        UserDTO currentUser = getCurrentUser(SecurityContextHolder.getContext().getAuthentication());

        if (currentUser == null) {
            return "redirect:/login";
        }

        List<CardDTO> userCards = cardService.getCardsByUserId(currentUser.getId());

        model.addAttribute("user", currentUser);
        model.addAttribute("cards", userCards);

        return "/user/deposit/createDeposit";
    }

    @PostMapping("/create")
    public String createDeposit(@RequestParam Long userId,
                                @RequestParam Long cardId,
                                @RequestParam BigDecimal amount,
                                RedirectAttributes redirectAttributes) {
        DepositDto depositDto = depositService.create(userId, cardId, amount);

        if (depositDto != null) {
            redirectAttributes.addFlashAttribute("deposit", depositDto);
            log.info("Deposit created: {}", depositDto.getAmount());
        } else {
            log.error("Deposit creation failed.");
        }

        return "redirect:/deposits/find";
    }


    @GetMapping("/find")
    public String findDepositsByUserAndCard(@RequestParam(required = false) Long cardId,
                                            Model model) {
        try {
            UserDTO currentUser = getCurrentUser(SecurityContextHolder.getContext().getAuthentication());

            if (currentUser == null) {
                return "redirect:/login";
            }

            List<CardDTO> userCards = cardService.getCardsByUserId(currentUser.getId());
            model.addAttribute("userCards", userCards);

            Long searchCardId = cardId;
            if (searchCardId == null && !userCards.isEmpty()) {
                searchCardId = userCards.get(0).getId();
            }

            if (searchCardId != null) {
                final Long finalCardId = searchCardId;
                boolean cardBelongsToUser = userCards.stream()
                        .anyMatch(card -> card.getId().equals(finalCardId));

                if (!cardBelongsToUser) {
                    model.addAttribute("error", "Card not found or doesn't belong to user");
                    return "user/deposit/depositList";
                }

                List<DepositDto> deposits = depositService.findDepositsByUserAndCard(currentUser.getId(), searchCardId);
                model.addAttribute("deposits", deposits);
                model.addAttribute("selectedCardId", searchCardId);
            }

            return "user/deposit/depositList";
        } catch (Exception e) {
            log.error("Error while finding deposits", e);
            model.addAttribute("error", "An error occurred while retrieving deposits");
            return "user/deposit/depositList";
        }
    }
    @PostMapping("/withdraw-all")
    public String withdrawAllFromDeposit(@RequestParam Long cardId,
                                         RedirectAttributes redirectAttributes) {
        try {
            UserDTO currentUser = getCurrentUser(SecurityContextHolder.getContext().getAuthentication());

            if (currentUser == null) {
                return "redirect:/login";
            }

            // Проверяем, принадлежит ли карта пользователю
            List<CardDTO> userCards = cardService.getCardsByUserId(currentUser.getId());
            boolean cardBelongsToUser = userCards.stream()
                    .anyMatch(card -> card.getId().equals(cardId));

            if (!cardBelongsToUser) {
                redirectAttributes.addFlashAttribute("error", "Card not found or doesn't belong to user");
                return "redirect:/deposits/find";
            }

            // Выполняем снятие средств
            DepositDto withdrawnDeposit = depositService.withdrawAllFromDeposit(currentUser.getId(), cardId);

            if (withdrawnDeposit != null) {
                redirectAttributes.addFlashAttribute("success",
                        String.format("Successfully withdrawn %.2f from deposit", withdrawnDeposit.getAmount()));
                log.info("Withdrawn all funds from deposit for user ID: {} and card ID: {}",
                        currentUser.getId(), cardId);
            } else {
                redirectAttributes.addFlashAttribute("error", "Failed to withdraw from deposit");
                log.error("Failed to withdraw from deposit for user ID: {} and card ID: {}",
                        currentUser.getId(), cardId);
            }

        } catch (Exception e) {
            log.error("Error while withdrawing from deposit", e);
            redirectAttributes.addFlashAttribute("error", "An error occurred while withdrawing from deposit");
        }

        return "redirect:/deposits/find";
    }



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
