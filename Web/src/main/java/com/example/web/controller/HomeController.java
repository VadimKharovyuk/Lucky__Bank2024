package com.example.web.controller;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import scala.Product;

import java.util.List;

@Controller
@AllArgsConstructor
public class HomeController {



    @GetMapping
    public String homePage() {
        return "/user/Home";
    }

}
