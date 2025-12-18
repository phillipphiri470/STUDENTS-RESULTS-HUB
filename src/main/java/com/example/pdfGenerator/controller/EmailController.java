package com.example.pdfGenerator.controller;

import com.example.pdfGenerator.service.EmailService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class EmailController {

    @Autowired
    private EmailService emailService;

    @PostMapping("/send-email")
    public String sendEmail(Model model) {

        emailService.sendSimpleEmail(
            "phillip.kamoto.phiri@cs.unza.zm",
            "Password Reset",
            "Hello,\n\nThis email was sent to reset your password,\n\nThank you"
        );

        model.addAttribute("message", "Email sent successfully!");
        return "Login"; // Thymeleaf page name
    }
}

