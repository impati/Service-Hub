package com.example.servicehub.web;

import com.example.servicehub.dto.ServiceSearchConditionForm;
import com.example.servicehub.service.CategoryAdminister;
import com.example.servicehub.service.ClientServiceAdminister;
import com.example.servicehub.web.util.ClientIdGetter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/client")
public class ClientController {

    private final ClientServiceAdminister clientServiceAdminister;
    private final CategoryAdminister categoryAdminister;

    @GetMapping
    public String renderClientPage(
            @RequestParam(value = "serviceName",required = false) String serviceName,
            UsernamePasswordAuthenticationToken authenticationToken,
            Model model){

        List<String> allCategories = categoryAdminister.getAllCategories();
        ServiceSearchConditionForm serviceSearchConditionForm = ServiceSearchConditionForm.of(categoryAdminister.getAllCategories(), serviceName);
        model.addAttribute("serviceWithClick",clientServiceAdminister.servicesOfClient(ClientIdGetter.getIdForm(authenticationToken), serviceSearchConditionForm).getContent()); // 사용자 처리
        model.addAttribute("allCategories",allCategories);

        return "/client/client-page";
    }

    @GetMapping("/click")
    public String clickService(
            @RequestParam Long serviceId,
            @RequestParam String serviceUrl,
            UsernamePasswordAuthenticationToken authenticationToken){

        clientServiceAdminister.countClickAndReturnUrl(ClientIdGetter.getIdForm(authenticationToken),serviceId);

        return "redirect:" + serviceUrl;
    }

}