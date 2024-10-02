package com.example.web.controller;

import com.example.web.Request.ReplenishCardRequest;
import com.example.web.dto.ReplenishmentDto;
import com.example.web.exeption.EntityNotFoundException;
import com.example.web.service.CardService;
import com.example.web.service.ReplenishmentService;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/replenish")
public class ReplenishmentController {
    private final ReplenishmentService replenishmentService;

    @GetMapping("/history/{cardId}")
    public String getReplenishmentHistory(@PathVariable Long cardId, Model model, RedirectAttributes redirectAttributes) {
        try {
            List<ReplenishmentDto> history = replenishmentService.getReplenishmentHistory(cardId);
            model.addAttribute("history", history);
            return "user/bankomat/history";
        } catch (EntityNotFoundException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/error";
        } catch (FeignException.InternalServerError e) {
            // Обработка ошибки 500 от Feign клиента
            model.addAttribute("history", new ArrayList<>());
            return "user/bankomat/history";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Произошла ошибка при получении истории пополнений.");
            return "redirect:/error";
        }
    }

    @GetMapping
    public String showReplenishForm(Model model) {
        model.addAttribute("replenishCardRequest", new ReplenishCardRequest());
        return "user/bankomat/replenish-form"; // имя HTML файла (например, replenish-form.html)
    }
    @PostMapping
    public String replenishCard(ReplenishCardRequest request, RedirectAttributes redirectAttributes) {
        String result = replenishmentService.replenishCard(request);
        redirectAttributes.addFlashAttribute("message", result);
        return "redirect:/replenish";
    }




}
