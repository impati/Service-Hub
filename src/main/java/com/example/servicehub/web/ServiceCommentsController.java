package com.example.servicehub.web;

import com.example.servicehub.dto.ServiceCommentForm;
import com.example.servicehub.dto.ServiceCommentUpdateForm;
import com.example.servicehub.service.ServiceCommentsAdminister;
import com.example.servicehub.service.ServiceSearch;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/comments")
public class ServiceCommentsController {

    private final ServiceCommentsAdminister serviceCommentsAdminister;
    private final ServiceSearch serviceSearch;


    @PostMapping
    public String addServiceComments(@ModelAttribute ServiceCommentForm serviceCommentForm){
        serviceCommentForm.setClientId(1L); // TODO : 사용자 처리
        serviceCommentsAdminister.addServiceComment(serviceCommentForm);
        return "redirect:/service/" + serviceCommentForm.getServiceId();
    }

    @GetMapping("/edit/{serviceId}/{commendId}")
    public String renderEditCommentPage(@PathVariable Long serviceId, @PathVariable Long commendId , Model model){
        model.addAttribute("singleServiceWithCommentsDto",serviceSearch.searchSingleService(serviceId,1L)); //  TODO : 사용자 처리
        model.addAttribute("commentId",commendId);
        model.addAttribute("commentContent",serviceCommentsAdminister.getCommentContent(commendId));
        return "/service/service-edit-page";
    }

    @PostMapping("/edit")
    public String updateComment(@ModelAttribute ServiceCommentUpdateForm serviceCommentUpdateForm){
        serviceCommentUpdateForm.setClientId(1L); // TODO : 사용자 처리
        serviceCommentsAdminister.updateServiceComment(serviceCommentUpdateForm);
        return "redirect:/service/" + serviceCommentUpdateForm.getServiceId();
    }

    @GetMapping("/delete/{serviceId}/{commendId}")
    public String deleteComment(@PathVariable Long serviceId,@PathVariable Long commendId){
        serviceCommentsAdminister.deleteServiceComment(commendId,1L); // TODO : 사용자 처리
        return "redirect:/service/" + serviceId;
    }


}
