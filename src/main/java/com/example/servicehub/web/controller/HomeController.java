package com.example.servicehub.web.controller;

import com.example.servicehub.security.SignupManager;
import com.example.servicehub.web.dto.SignupForm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;

@Slf4j
@Controller
@RequiredArgsConstructor
public class HomeController {

    private final SignupManager signupManager;

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
    public String signup(@Valid @ModelAttribute SignupForm signupForm, BindingResult bindingResult){

        if(!signupForm.isSamePassword()) bindingResult.rejectValue("repeatPassword",null,"비밀번호가 일치하지 않습니다.");

        if(bindingResult.hasErrors()) {
            return "client/signup";
        }

        signupManager.signup(signupForm);

        return "redirect:/login";
    }

}
