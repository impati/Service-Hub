package com.example.servicehub.web.controller;

import com.example.servicehub.util.ProjectUtils;
import com.example.servicehub.web.dto.SignupForm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;

@Slf4j
@Controller
@RequiredArgsConstructor
public class HomeController {

    @GetMapping("/")
    public String home(){
        return "redirect:/service/search";
    }

    @GetMapping("/login")
    public String renderLoginPageWhenHasError(@RequestParam(name = "error",required = false) Boolean hasError,
                                  Model model){

        model.addAttribute("error",hasError);

        return "client/signin";
    }

    @GetMapping("/signup")
    public String renderSignupPage(Model model) {

        model.addAttribute("signupForm",new SignupForm());

        return "client/signup";
    }

    @PostMapping("/signup")
    public String signup(@Valid @ModelAttribute SignupForm signupForm,
                         BindingResult bindingResult,
                         RedirectAttributes redirectAttributes){

        if(!signupForm.isSamePassword()) bindingResult.rejectValue("repeatPassword",null,"비밀번호가 일치하지 않습니다.");

        if(bindingResult.hasErrors()) {
            return "client/signup";
        }

        redirectAttributes.addAttribute("username",signupForm.getUsername());
        redirectAttributes.addAttribute("email",signupForm.getEmail());
        redirectAttributes.addAttribute("password",signupForm.getPassword());

        return "redirect:/keycloak/signup";
    }

}
