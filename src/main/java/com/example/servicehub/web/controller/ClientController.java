package com.example.servicehub.web.controller;

import com.example.servicehub.dto.CustomServiceForm;
import com.example.servicehub.dto.ServiceSearchConditionForm;
import com.example.servicehub.security.authentication.CustomerPrincipal;
import com.example.servicehub.service.CategoryAdminister;
import com.example.servicehub.service.ClientServiceAdminister;
import com.example.servicehub.service.CustomServiceAdminister;
import com.example.servicehub.web.dto.SimpleClientDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
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
    private final CustomServiceAdminister customServiceAdminister;

    @GetMapping("/{clientId}")
    public String renderClientPage(
            @PathVariable Long clientId,
            @RequestParam(value = "serviceName", required = false) String serviceName,
            Model model) {

        List<String> allCategories = categoryAdminister.getAllCategories();

        ServiceSearchConditionForm serviceSearchConditionForm = ServiceSearchConditionForm.of(allCategories, serviceName);

        model.addAttribute("serviceWithClick", clientServiceAdminister.servicesOfClient(clientId, serviceSearchConditionForm));

        model.addAttribute("allCategories", allCategories);

        model.addAttribute("simpleClient", simpleClientDto());

        return "client/client-page";
    }

    private SimpleClientDto simpleClientDto() {
        CustomerPrincipal customerPrincipal = getCustomerPrincipal();
        return SimpleClientDto.builder()
                .nickname(customerPrincipal.getNickname())
                .clientId(customerPrincipal.getId())
                .blogUrl(customerPrincipal.getBlogUrl())
                .profileUrl(customerPrincipal.getProfileImageUrl())
                .introComment(customerPrincipal.getIntroduceComment())
                .build();
    }

    private CustomerPrincipal getCustomerPrincipal() {
        return (CustomerPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }


    @GetMapping("/service/edit")
    public String renderClientServiceEdit(@AuthenticationPrincipal CustomerPrincipal customerPrincipal,
                                          @RequestParam(value = "serviceName", required = false) String serviceName,
                                          Model model) {

        List<String> allCategories = categoryAdminister.getAllCategories();

        ServiceSearchConditionForm serviceSearchConditionForm = ServiceSearchConditionForm.of(categoryAdminister.getAllCategories(), serviceName);

        model.addAttribute("serviceWithClick", clientServiceAdminister.servicesOfClient(customerPrincipal.getId(), serviceSearchConditionForm));

        model.addAttribute("allCategories", allCategories);

        model.addAttribute("simpleClient", simpleClientDto());

        return "client/client-service-edit";
    }

    @ResponseBody
    @PostMapping("/service/delete/{serviceId}")
    public String editClientService(@PathVariable Long serviceId,
                                    @RequestParam(defaultValue = "false") boolean isCustom,
                                    @AuthenticationPrincipal CustomerPrincipal customerPrincipal) {

        clientServiceAdminister.deleteClientService(customerPrincipal.getId(), serviceId);

        return "Ok";
    }

    @GetMapping("/click")
    public String clickService(
            @RequestParam Long serviceId,
            @RequestParam(defaultValue = "false") boolean isCustom,
            @AuthenticationPrincipal CustomerPrincipal customerPrincipal) {


        String serviceUrl = clientServiceAdminister.countClickAndReturnUrl(customerPrincipal.getId(), serviceId);

        return "redirect:" + serviceUrl;
    }

    @ResponseBody
    @PostMapping("/add-service/{serviceId}")
    public String addClientService(@PathVariable Long serviceId, @AuthenticationPrincipal CustomerPrincipal customerPrincipal) {

        clientServiceAdminister.addClientService(customerPrincipal.getId(), serviceId);

        return "Ok";
    }

    @ResponseBody
    @PostMapping("/delete-service/{serviceId}")
    public String deleteClientService(@PathVariable Long serviceId, @AuthenticationPrincipal CustomerPrincipal customerPrincipal) {

        clientServiceAdminister.deleteClientService(customerPrincipal.getId(), serviceId);

        return "Ok";
    }


    @ResponseBody
    @PostMapping("/add-custom")
    public String addCustomService(@ModelAttribute CustomServiceForm customServiceForm,
                                   @AuthenticationPrincipal CustomerPrincipal customerPrincipal) {

        customServiceAdminister.addCustomService(customerPrincipal.getId(), customServiceForm);

        return "Ok";
    }

}
