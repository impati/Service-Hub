package com.example.servicehub.web.controller;

import com.example.servicehub.dto.ServiceCommentForm;
import com.example.servicehub.dto.ServiceCommentUpdateForm;
import com.example.servicehub.security.authentication.CustomerPrincipal;
import com.example.servicehub.service.ServiceCommentsAdminister;
import com.example.servicehub.service.SingleServiceSearch;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.Optional;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/comments")
public class ServiceCommentsController {

    private final ServiceCommentsAdminister serviceCommentsAdminister;
    private final SingleServiceSearch searchSingleService;

    @PostMapping
    public String addServiceComments(@Valid @ModelAttribute ServiceCommentForm serviceCommentForm,
                                     BindingResult bindingResult,
                                     RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            redirectAttributes.addAttribute("contentError", bindingResult.getFieldError().getDefaultMessage());
            redirectAttributes.addAttribute("hasError", true);
            return "redirect:/service/" + serviceCommentForm.getServiceId();
        }

        serviceCommentsAdminister.addServiceComment(serviceCommentForm);

        return "redirect:/service/" + serviceCommentForm.getServiceId();
    }

    @GetMapping("/edit")
    public String renderEditCommentPage(@ModelAttribute ServiceCommentUpdateForm serviceCommentUpdateForm,
                                        @AuthenticationPrincipal CustomerPrincipal customerPrincipal,
                                        Model model) {

        model.addAttribute("singleServiceWithCommentsDto"
                , searchSingleService.searchWithComments(serviceCommentUpdateForm.getServiceId(), Optional.ofNullable(customerPrincipal.getId())));

        model.addAttribute("commentContent", serviceCommentsAdminister.bringCommentContent(serviceCommentUpdateForm.getCommentId()));

        return "service/service-edit-page";
    }

    @PostMapping("/edit")
    public String updateComment(@Valid @ModelAttribute ServiceCommentUpdateForm serviceCommentUpdateForm,
                                BindingResult bindingResult,
                                @AuthenticationPrincipal CustomerPrincipal customerPrincipal) {

        if (bindingResult.hasErrors()) return "/service/service-edit-page";

        serviceCommentUpdateForm.assignClient(customerPrincipal.getId());

        serviceCommentsAdminister.updateServiceComment(serviceCommentUpdateForm);

        return "redirect:/service/" + serviceCommentUpdateForm.getServiceId();
    }

    @ResponseBody
    @DeleteMapping("/delete")
    public String deleteComment(@ModelAttribute ServiceCommentUpdateForm serviceCommentUpdateForm,
                                @AuthenticationPrincipal CustomerPrincipal customerPrincipal) {

        serviceCommentsAdminister.deleteServiceComment(serviceCommentUpdateForm.getCommentId(), customerPrincipal.getId());

        return "OK";
    }

}
