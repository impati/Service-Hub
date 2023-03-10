package com.example.servicehub.web.controller;

import com.example.servicehub.dto.ClientEditForm;
import com.example.servicehub.dto.CustomServiceForm;
import com.example.servicehub.dto.ServiceSearchConditionForm;
import com.example.servicehub.security.authentication.ClientPrincipal;
import com.example.servicehub.service.CategoryAdminister;
import com.example.servicehub.service.ClientAdminister;
import com.example.servicehub.service.ClientServiceAdminister;
import com.example.servicehub.service.CustomServiceAdminister;
import com.example.servicehub.web.dto.SimpleClientDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/client")
public class ClientController {

    private final ClientServiceAdminister clientServiceAdminister;
    private final CategoryAdminister categoryAdminister;
    private final ClientAdminister clientAdminister;
    private final CustomServiceAdminister customServiceAdminister;

    @GetMapping("/{clientId}")
    public String renderClientPage(
            @PathVariable Long clientId,
            @RequestParam(value = "serviceName",required = false) String serviceName,
            Model model){

        List<String> allCategories = categoryAdminister.getAllCategories();

        ServiceSearchConditionForm serviceSearchConditionForm = ServiceSearchConditionForm.of(allCategories, serviceName);

        model.addAttribute("serviceWithClick",clientServiceAdminister.servicesOfClient(clientId, serviceSearchConditionForm));

        model.addAttribute("allCategories",allCategories);

        model.addAttribute("simpleClient", SimpleClientDto.from(clientAdminister.findClientByClientId(clientId)));

        return "client/client-page";
    }

    @GetMapping("/edit")
    public String renderClientProfileEditPage(
            @AuthenticationPrincipal ClientPrincipal clientPrincipal,
            Model model){

        model.addAttribute("clientEditForm",
                ClientEditForm.from(clientAdminister.findClientByClientId(clientPrincipal.getId())));

        return "client/client-edit-page";
    }

    @PostMapping("/edit")
    public String editClientProfile(
            @AuthenticationPrincipal ClientPrincipal clientPrincipal,
            @Valid @ModelAttribute ClientEditForm clientEditForm , BindingResult bindingResult){

        if(bindingResult.hasErrors()){
            return "client/client-edit-page";
        }

        clientAdminister.editClientProfile(clientPrincipal.getId(),clientEditForm);

        clientPrincipal.editNickname(clientEditForm.getNickname());

        return "redirect:/client/" + clientPrincipal.getId();
    }

    @GetMapping("/service/edit")
    public String renderClientServiceEdit(@AuthenticationPrincipal ClientPrincipal clientPrincipal,
                                          @RequestParam(value = "serviceName",required = false) String serviceName,
                                          Model model){

        List<String> allCategories = categoryAdminister.getAllCategories();

        ServiceSearchConditionForm serviceSearchConditionForm = ServiceSearchConditionForm.of(categoryAdminister.getAllCategories(), serviceName);

        model.addAttribute("serviceWithClick",clientServiceAdminister.servicesOfClient(clientPrincipal.getId(), serviceSearchConditionForm));

        model.addAttribute("allCategories",allCategories);

        model.addAttribute("simpleClient", SimpleClientDto.from(clientAdminister.findClientByClientId(clientPrincipal.getId())));

        return "client/client-service-edit";
    }

    @ResponseBody
    @PostMapping("/service/delete/{serviceId}")
    public String editClientService(@PathVariable Long serviceId,
                                    @RequestParam(defaultValue = "false") boolean isCustom,
                                    @AuthenticationPrincipal ClientPrincipal clientPrincipal){

        clientServiceAdminister.deleteClientService(clientPrincipal.getId(),serviceId,isCustom);

        return "Ok";
    }

    @GetMapping("/click")
    public String clickService(
            @RequestParam Long serviceId,
            @RequestParam(defaultValue = "false") boolean isCustom,
            @AuthenticationPrincipal ClientPrincipal clientPrincipal){


        String serviceUrl = clientServiceAdminister.countClickAndReturnUrl(clientPrincipal.getId(),serviceId,isCustom);

        return "redirect:" + serviceUrl;
    }

    @ResponseBody
    @PostMapping("/add-service/{serviceId}")
    public String addClientService(@PathVariable Long serviceId, @AuthenticationPrincipal ClientPrincipal clientPrincipal){

        clientServiceAdminister.addClientService(clientPrincipal.getId(), serviceId);

        return "Ok";
    }

    @ResponseBody
    @PostMapping("/delete-service/{serviceId}")
    public String deleteClientService(@PathVariable Long serviceId, @AuthenticationPrincipal ClientPrincipal clientPrincipal){

        clientServiceAdminister.deleteClientService(clientPrincipal.getId(), serviceId,false);

        return "Ok";
    }



    @ResponseBody
    @PostMapping("/add-custom")
    public String addCustomService(@ModelAttribute CustomServiceForm customServiceForm,
                                   @AuthenticationPrincipal ClientPrincipal clientPrincipal){

        customServiceAdminister.addCustomService(clientPrincipal.getId(),customServiceForm);

        return "Ok";
    }

}
