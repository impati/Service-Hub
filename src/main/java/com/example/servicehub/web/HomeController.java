package com.example.servicehub.web;

import com.example.servicehub.dto.ClientRegisterForm;
import com.example.servicehub.service.ClientServiceAdminister;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Slf4j
@Controller
@RequiredArgsConstructor
public class HomeController {

    private final ClientServiceAdminister clientServiceAdminister;
    private final PasswordEncoder passwordEncoder;

    @GetMapping("/")
    public String home(){
        return "redirect:/service/search";
    }

    @GetMapping("/login")
    public String loginPage(){
        return "client/signin";
    }


    @GetMapping("/signup")
    public String signup() {
        return "client/signup";
    }

    @PostMapping("/signup")
    public String signup(@ModelAttribute ClientRegisterForm clientRegisterForm) {

        clientRegisterForm.setEncode(passwordEncoder.encode(clientRegisterForm.getPassword()));

        clientServiceAdminister.registerClient(clientRegisterForm.toEntity());

        return "redirect:/client";
    }

}
