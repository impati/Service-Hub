package com.example.servicehub.web;

import com.example.servicehub.dto.ClientEditForm;
import com.example.servicehub.dto.ServiceSearchConditionForm;
import com.example.servicehub.dto.SimpleClientDto;
import com.example.servicehub.security.authentication.ClientPrincipal;
import com.example.servicehub.service.CategoryAdminister;
import com.example.servicehub.service.ClientAdminister;
import com.example.servicehub.service.ClientServiceAdminister;
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

    @GetMapping("/{clientId}")
    public String renderClientPage(
            @PathVariable Long clientId,
            @RequestParam(value = "serviceName",required = false) String serviceName,
            Model model){

        List<String> allCategories = categoryAdminister.getAllCategories();

        ServiceSearchConditionForm serviceSearchConditionForm = ServiceSearchConditionForm.of(categoryAdminister.getAllCategories(), serviceName);

        model.addAttribute("serviceWithClick",clientServiceAdminister.servicesOfClient(clientId, serviceSearchConditionForm).getContent());

        model.addAttribute("allCategories",allCategories);

        model.addAttribute("simpleClient", SimpleClientDto.from(clientAdminister.findClientByClientId(clientId)));

        return "/client/client-page";
    }

    @GetMapping("/edit")
    public String renderClientEditPage(
            @AuthenticationPrincipal ClientPrincipal clientPrincipal,
            Model model){

        model.addAttribute("clientEditForm",
                ClientEditForm.from(clientAdminister.findClientByClientId(clientPrincipal.getId())));

        return "/client/client-edit-page";
    }

    @PostMapping("/edit")
    public String editClient(
            @AuthenticationPrincipal ClientPrincipal clientPrincipal,
            @Valid @ModelAttribute ClientEditForm clientEditForm , BindingResult bindingResult){

        if(bindingResult.hasErrors()){
            return "/client/client-edit-page";
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

        model.addAttribute("serviceWithClick",clientServiceAdminister.servicesOfClient(clientPrincipal.getId(), serviceSearchConditionForm).getContent());

        model.addAttribute("allCategories",allCategories);

        model.addAttribute("simpleClient", SimpleClientDto.from(clientAdminister.findClientByClientId(clientPrincipal.getId())));

        return "/client/client-service-edit";
    }

    @ResponseBody
    @PostMapping("/service/delete/{serviceId}")
    public String renderClientServiceEdit(@PathVariable Long serviceId,
                                          @AuthenticationPrincipal ClientPrincipal clientPrincipal){

        clientServiceAdminister.deleteClientService(clientPrincipal.getId(),serviceId);

        return "Ok";
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
