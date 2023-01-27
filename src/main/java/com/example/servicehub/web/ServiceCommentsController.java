package com.example.servicehub.web;

import com.example.servicehub.dto.ServiceCommentForm;
import com.example.servicehub.dto.ServiceCommentUpdateForm;
import com.example.servicehub.service.ServiceCommentsAdminister;
import com.example.servicehub.service.ServiceSearch;
import com.example.servicehub.web.util.ClientIdGetter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
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
                                     UsernamePasswordAuthenticationToken authenticationToken){
        serviceCommentForm.assignAnAuthor(ClientIdGetter.getIdFrom(authenticationToken));
        serviceCommentsAdminister.addServiceComment(serviceCommentForm);
        return "redirect:/service/" + serviceCommentForm.getServiceId();
    }

    @GetMapping("/edit")
    public String renderEditCommentPage(@ModelAttribute ServiceCommentUpdateForm serviceCommentUpdateForm,
                                        UsernamePasswordAuthenticationToken authenticationToken,
                                        Model model){

        model.addAttribute("singleServiceWithCommentsDto"
                ,serviceSearch.searchSingleService(serviceCommentUpdateForm.getServiceId(), Optional.ofNullable(ClientIdGetter.getIdFrom(authenticationToken))));
        model.addAttribute("commentId",serviceCommentUpdateForm.getCommentId());
        model.addAttribute("commentContent",serviceCommentsAdminister.getCommentContent(serviceCommentUpdateForm.getCommentId()));
        return "/service/service-edit-page";
    }

    @PostMapping("/edit")
    public String updateComment(@ModelAttribute ServiceCommentUpdateForm serviceCommentUpdateForm,
                                UsernamePasswordAuthenticationToken authenticationToken){
        serviceCommentUpdateForm.assignClient(ClientIdGetter.getIdFrom(authenticationToken));
        serviceCommentsAdminister.updateServiceComment(serviceCommentUpdateForm);
        return "redirect:/service/" + serviceCommentUpdateForm.getServiceId();
    }

    @GetMapping("/delete")
    public String deleteComment(@ModelAttribute ServiceCommentUpdateForm serviceCommentUpdateForm,
                                UsernamePasswordAuthenticationToken authenticationToken){
        serviceCommentsAdminister.deleteServiceComment(serviceCommentUpdateForm.getCommentId(),
                ClientIdGetter.getIdFrom(authenticationToken));
        return "redirect:/service/" + serviceCommentUpdateForm.getServiceId();
    }


}
