package com.example.web.controller;

import com.example.web.Request.CreditRequestDto;
import com.example.web.dto.CreditDto;
import com.example.web.dto.UserDTO;
import com.example.web.service.CardService;
import com.example.web.service.CreditService;
import com.example.web.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.util.List;

@Controller
@RequestMapping("/credits")
@RequiredArgsConstructor
public class CreditWebController {

    private final CreditService creditService;
    private final UserService userService;
    private final CardService cardService;


    @GetMapping("/create")
    public String showCreateForm(Model model) {
        UserDTO currentUser = getCurrentUser();
        if (currentUser == null) {
            return "redirect:/login";
        }
        model.addAttribute("creditForm", new CreditRequestDto());
        model.addAttribute("cards", cardService.getCardsByUserId(currentUser.getId()));
        model.addAttribute("user", currentUser);
        return "user/credit/creditForm";
    }

    @PostMapping("/create")
    public String createCredit(@ModelAttribute("creditForm") CreditRequestDto creditRequestDto,
                               BindingResult bindingResult,
                               RedirectAttributes redirectAttributes) {
        UserDTO currentUser = getCurrentUser();
        if (currentUser == null) {
            return "redirect:/login";
        }

        if (bindingResult.hasErrors()) {
            return "user/credit/creditForm";
        }

        creditRequestDto.setUserId(currentUser.getId());
        CreditDto createdCredit = creditService.create(creditRequestDto);
        redirectAttributes.addFlashAttribute("message", "Кредит успешно создан");
        return "redirect:/credits";
    }

    @PostMapping("/{creditId}/approve")
    public String approveCredit(@PathVariable Long creditId, RedirectAttributes redirectAttributes) {
        UserDTO currentUser = getCurrentUser();
        if (currentUser == null) {
            return "redirect:/login";
        }
        creditService.approveCredit(creditId);
        redirectAttributes.addFlashAttribute("message", "Кредит одобрен");
        return "redirect:/credits";
    }

    @PostMapping("/{creditId}/payment")
    public String makePayment(@PathVariable Long creditId,
                              @RequestParam BigDecimal paymentAmount,
                              RedirectAttributes redirectAttributes) {
        UserDTO currentUser = getCurrentUser();
        if (currentUser == null) {
            return "redirect:/login";
        }
        creditService.makePayment(creditId, paymentAmount);
        redirectAttributes.addFlashAttribute("message", "Платеж выполнен успешно");
        return "redirect:/credits";
    }

    @PostMapping("/{creditId}/delete")
    public String deleteCredit(@PathVariable Long creditId, RedirectAttributes redirectAttributes) {
        UserDTO currentUser = getCurrentUser();
        if (currentUser == null) {
            return "redirect:/login";
        }
        creditService.deleteCreditById(creditId);
        redirectAttributes.addFlashAttribute("message", "Кредит удален");
        return "redirect:/credits";
    }

    private UserDTO getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return getCurrentUser(authentication);
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