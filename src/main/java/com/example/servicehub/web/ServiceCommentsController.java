package com.example.servicehub.web;

import com.example.servicehub.dto.ServiceCommentForm;
import com.example.servicehub.dto.ServiceCommentUpdateForm;
import com.example.servicehub.security.authentication.ClientPrincipal;
import com.example.servicehub.service.ServiceCommentsAdminister;
import com.example.servicehub.service.ServiceSearch;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/comments")
public class ServiceCommentsController {

    private final ServiceCommentsAdminister serviceCommentsAdminister;
    private final ServiceSearch serviceSearch;

    @PostMapping
    public String addServiceComments(@ModelAttribute ServiceCommentForm serviceCommentForm,
                                     @AuthenticationPrincipal ClientPrincipal clientPrincipal){

        serviceCommentForm.assignAnAuthor(clientPrincipal.getId());

        serviceCommentsAdminister.addServiceComment(serviceCommentForm);

        return "redirect:/service/" + serviceCommentForm.getServiceId();
    }

    @GetMapping("/edit")
    public String renderEditCommentPage(@ModelAttribute ServiceCommentUpdateForm serviceCommentUpdateForm,
                                        @AuthenticationPrincipal ClientPrincipal clientPrincipal,
                                        Model model){

        model.addAttribute("singleServiceWithCommentsDto"
                ,serviceSearch.searchSingleService(serviceCommentUpdateForm.getServiceId(), Optional.ofNullable(clientPrincipal.getId())));

        model.addAttribute("commentId",serviceCommentUpdateForm.getCommentId());

        model.addAttribute("commentContent",serviceCommentsAdminister.getCommentContent(serviceCommentUpdateForm.getCommentId()));

        return "/service/service-edit-page";
    }

    @PostMapping("/edit")
    public String updateComment(@ModelAttribute ServiceCommentUpdateForm serviceCommentUpdateForm,
                                @AuthenticationPrincipal ClientPrincipal clientPrincipal){

        serviceCommentUpdateForm.assignClient(clientPrincipal.getId());

        serviceCommentsAdminister.updateServiceComment(serviceCommentUpdateForm);

        return "redirect:/service/" + serviceCommentUpdateForm.getServiceId();
    }

    @GetMapping("/delete")
    public String deleteComment(@ModelAttribute ServiceCommentUpdateForm serviceCommentUpdateForm,
                                @AuthenticationPrincipal ClientPrincipal clientPrincipal){

        serviceCommentsAdminister.deleteServiceComment(serviceCommentUpdateForm.getCommentId(),clientPrincipal.getId());

        return "redirect:/service/" + serviceCommentUpdateForm.getServiceId();
    }


}
