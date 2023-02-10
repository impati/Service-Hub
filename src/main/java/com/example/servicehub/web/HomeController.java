package com.example.servicehub.web;

import com.example.servicehub.dto.SignupForm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

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
