package com.example.web.controller;

import com.example.web.dto.CardDTO;
import com.example.web.dto.UserDTO;
import com.example.web.service.CardService;
import com.example.web.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/dashboard")
@AllArgsConstructor
public class DashboardController {

    private final CardService cardService;
    private final UserService userService;


    @GetMapping
    public String dashboard(Model model) {

return "user/dashbord/Personal Bank Account";
    }


}