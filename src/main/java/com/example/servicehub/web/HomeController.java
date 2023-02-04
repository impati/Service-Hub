package com.example.servicehub.web;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@Controller
@RequiredArgsConstructor
public class HomeController {

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

}
