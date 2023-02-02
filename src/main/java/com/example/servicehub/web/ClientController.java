package com.example.servicehub.web;

import com.example.servicehub.dto.ServiceSearchConditionForm;
import com.example.servicehub.security.authentication.ClientPrincipal;
import com.example.servicehub.service.CategoryAdminister;
import com.example.servicehub.service.ClientServiceAdminister;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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
            @AuthenticationPrincipal ClientPrincipal clientPrincipal,
            Model model){

        List<String> allCategories = categoryAdminister.getAllCategories();

        ServiceSearchConditionForm serviceSearchConditionForm = ServiceSearchConditionForm.of(categoryAdminister.getAllCategories(), serviceName);

        model.addAttribute("serviceWithClick",clientServiceAdminister.servicesOfClient(clientPrincipal.getId(), serviceSearchConditionForm).getContent());

        model.addAttribute("allCategories",allCategories);

        return "/client/client-page";
    }

    @GetMapping("/click")
    public String clickService(
            @RequestParam Long serviceId,
            @RequestParam String serviceUrl,
            @AuthenticationPrincipal ClientPrincipal clientPrincipal){

        clientServiceAdminister.countClickAndReturnUrl(clientPrincipal.getId(),serviceId);

        return "redirect:" + serviceUrl;
    }

    @ResponseBody
    @PostMapping("/add-service/{serviceId}")
    public String addClientService(@PathVariable Long serviceId, @AuthenticationPrincipal ClientPrincipal clientContext){

        clientServiceAdminister.addClientService(clientContext.getId(), serviceId);

        return "Ok";
    }

    @ResponseBody
    @PostMapping("/delete-service/{serviceId}")
    public String deleteClientService(@PathVariable Long serviceId, @AuthenticationPrincipal ClientPrincipal clientContext){

        clientServiceAdminister.deleteClientService(clientContext.getId(), serviceId);

        return "Ok";
    }

}
