package com.example.servicehub.web.controller;

import com.example.servicehub.dto.ClickServiceDto;
import com.example.servicehub.dto.CustomServiceForm;
import com.example.servicehub.dto.ServiceSearchConditionForm;
import com.example.servicehub.security.authentication.CustomerPrincipal;
import com.example.servicehub.service.*;
import com.example.servicehub.web.dto.SimpleClientDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/client")
public class ClientController {

    private final CustomerCustomServiceAdminister customerCustomServiceAdminister;
    private final CustomerServiceAdminister customerServiceAdminister;
    private final CategoryAdminister categoryAdminister;
    private final ServiceClickCounter serviceClickCounter;
    private final CustomerServiceSearch customerServiceSearch;

    @GetMapping("/{clientId}")
    public String renderClientPage(
            @PathVariable Long clientId,
            @RequestParam(value = "serviceName", required = false) String serviceName,
            Model model) {

        List<String> allCategories = categoryAdminister.getAllCategories();

        model.addAttribute("serviceWithClick", findResult(clientId, ServiceSearchConditionForm.of(allCategories, serviceName)));

        model.addAttribute("allCategories", allCategories);

        model.addAttribute("simpleClient", simpleClientDto());

        return "client/client-page";
    }

    @GetMapping("/service/edit")
    public String renderClientServiceEdit(@AuthenticationPrincipal CustomerPrincipal customerPrincipal,
                                          @RequestParam(value = "serviceName", required = false) String serviceName,
                                          Model model) {

        List<String> allCategories = categoryAdminister.getAllCategories();

        model.addAttribute("serviceWithClick", findResult(customerPrincipal.getId(), ServiceSearchConditionForm.of(allCategories, serviceName)));

        model.addAttribute("allCategories", allCategories);

        model.addAttribute("simpleClient", simpleClientDto());

        return "client/client-service-edit";
    }

    @ResponseBody
    @PostMapping("/service/delete/{serviceId}")
    public String editClientService(@PathVariable Long serviceId,
                                    @RequestParam(defaultValue = "false") Boolean isCustom,
                                    @AuthenticationPrincipal CustomerPrincipal customerPrincipal) {
        if (isCustom) customerCustomServiceAdminister.deleteCustomService(customerPrincipal.getId(), serviceId); //TODO
        else customerServiceAdminister.deleteClientService(customerPrincipal.getId(), serviceId);
        return "Ok";
    }

    @GetMapping("/click")
    public String clickService(
            @RequestParam Long serviceId,
            @RequestParam(defaultValue = "false") Boolean isCustom,
            @AuthenticationPrincipal CustomerPrincipal customerPrincipal) {
        String serviceUrl = "";
        if (isCustom)
            serviceUrl = customerCustomServiceAdminister.countClickAndReturnUrl(customerPrincipal.getId(), serviceId);
        else serviceUrl = serviceClickCounter.countClickAndReturnUrl(customerPrincipal.getId(), serviceId);
        return "redirect:" + serviceUrl;
    }

    @ResponseBody
    @PostMapping("/add-service/{serviceId}")
    public String addClientService(@PathVariable Long serviceId, @AuthenticationPrincipal CustomerPrincipal customerPrincipal) {

        customerServiceAdminister.addClientService(customerPrincipal.getId(), serviceId);

        return "Ok";
    }

    @ResponseBody
    @PostMapping("/delete-service/{serviceId}")
    public String deleteClientService(@PathVariable Long serviceId, @AuthenticationPrincipal CustomerPrincipal customerPrincipal) {

        customerServiceAdminister.deleteClientService(customerPrincipal.getId(), serviceId);

        return "Ok";
    }

    @ResponseBody
    @PostMapping("/add-custom")
    public String addCustomService(@ModelAttribute CustomServiceForm customServiceForm,
                                   @AuthenticationPrincipal CustomerPrincipal customerPrincipal) {
        customerCustomServiceAdminister.addCustomService(customerPrincipal.getId(), customServiceForm);
        return "Ok";
    }

    private List<ClickServiceDto> findResult(Long clientId, ServiceSearchConditionForm serviceSearchConditionForm) {
        List<ClickServiceDto> result = new ArrayList<>();
        result.addAll(customerServiceSearch.customServicesOfClient(clientId, serviceSearchConditionForm.getServiceName())
                .stream()
                .map(ClickServiceDto::from)
                .collect(toList()));
        result.addAll(customerServiceSearch.servicesOfClient(clientId, serviceSearchConditionForm));
        return result;
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

}
