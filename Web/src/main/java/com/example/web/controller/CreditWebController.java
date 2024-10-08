package com.example.web.controller;

import com.example.web.Request.CreditRequestDto;
import com.example.web.dto.CardDTO;
import com.example.web.dto.CreditDto;
import com.example.web.dto.PaymentScheduleDto;
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


    //оплата кредита
    @PostMapping("/{creditId}/payment")
    public String makePayment(@PathVariable Long creditId,
                              @RequestParam BigDecimal paymentAmount,
                              RedirectAttributes redirectAttributes) {
        try {
            creditService.makePayment(creditId, paymentAmount);
            redirectAttributes.addFlashAttribute("message", "Платеж выполнен успешно");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Произошла ошибка при выполнении платежа");
        }
        return "redirect:/credits/list";
    }


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
        return "redirect:/credits/list";
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


///исправить метод получание кредита по 1 карте
@GetMapping("/list")
public String getCreditsByUserId(Model model, @RequestParam(required = false) Long cardId) {
    UserDTO currentUser = getCurrentUser();
    if (currentUser == null) {
        return "redirect:/login";
    }

    // Получаем список карт пользователя
    List<CardDTO> userCards = cardService.getCardsByUserId(currentUser.getId());

    // Проверяем, есть ли карты у пользователя
    if (userCards.isEmpty()) {
        model.addAttribute("errorMessage", "У вас нет карт для получения кредитов.");
        return "user/credit/listCreditByUser";
    }

    // Если cardId не указан, не выбираем карту по умолчанию
    List<CreditDto> creditList;
    if (cardId == null) {
        // Получаем кредиты для всех карт пользователя
        creditList = creditService.getCreditsByUser(currentUser.getId());
    } else {
        // Получаем кредиты для конкретной карты
        creditList = creditService.getCreditsByUserAndCard(currentUser.getId(), cardId);
    }

    model.addAttribute("cards", userCards);
    model.addAttribute("selectedCardId", cardId);
    model.addAttribute("list", creditList);
    model.addAttribute("user", currentUser);

    return "user/credit/listCreditByUser";
}
//    @GetMapping("/list")
//    public String getCreditsByUserId(Model model, @RequestParam(required = false) Long cardId) {
//        UserDTO currentUser = getCurrentUser();
//        if (currentUser == null) {
//            return "redirect:/login";
//        }
//
//        // Получаем список карт пользователя
//        List<CardDTO> userCards = cardService.getCardsByUserId(currentUser.getId());
//
//        // Проверяем, есть ли карты у пользователя
//        if (userCards.isEmpty()) {
//            model.addAttribute("errorMessage", "У вас нет карт для получения кредитов.");
//            return "user/credit/listCreditByUser"; // Верните на страницу с сообщением об ошибке
//        }
//
//        // Если карта не выбрана, используем первую карту
//        if (cardId == null && !userCards.isEmpty()) {
//            cardId = userCards.get(0).getId(); // Получаем ID первой карты
//        }
//
//
//        // Получаем кредиты для пользователя и карты
//        List<CreditDto> creditList = creditService.getCreditsByUserAndCard(currentUser.getId(), cardId);
//        model.addAttribute("cards", userCards);
//        model.addAttribute("selectedCardId", cardId);
//        model.addAttribute("list", creditList);
//        model.addAttribute("user", currentUser);
//
//        return "user/credit/listCreditByUser";
//    }

    @GetMapping("/listSchedule/{creditId}")
    public String getAllCreditSchedule(@PathVariable Long creditId, Model model) {
        // Получаем список всех графиков платежей через сервис
        List<PaymentScheduleDto> scheduleDtos = creditService.getPaymentSchedulesByCreditId(creditId);

//        // Получаем информацию о кредите (предположим, что у вас есть метод getCreditById)
//        CreditDto credit = creditService.getCreditById(creditId);

        // Добавляем список графиков платежей в модель
        model.addAttribute("schedules", scheduleDtos);

        // Добавляем информацию о кредите в модель
//        model.addAttribute("credit", credit);

        // Возвращаем название страницы для отображения
        return "/user/credit/listSchedule";
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