package com.example.servicehub.web.controller.customer;

import com.example.servicehub.dto.custom.CustomServiceForm;
import com.example.servicehub.dto.services.ClickServiceDto;
import com.example.servicehub.dto.services.ServiceSearchConditionForm;
import com.example.servicehub.security.authentication.CustomerPrincipal;
import com.example.servicehub.service.category.CategoryAdminister;
import com.example.servicehub.service.customer.CustomerCustomServiceAdminister;
import com.example.servicehub.service.customer.CustomerEditor;
import com.example.servicehub.service.customer.CustomerServiceAdminister;
import com.example.servicehub.service.customer.CustomerServiceSearch;
import com.example.servicehub.service.services.ServiceClickCounter;
import com.example.servicehub.web.dto.customer.CustomerEditForm;
import com.example.servicehub.web.dto.customer.SimpleCustomerDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/customer")
public class CustomerController {

    private final CustomerCustomServiceAdminister customerCustomServiceAdminister;
    private final CustomerServiceAdminister customerServiceAdminister;
    private final CategoryAdminister categoryAdminister;
    private final ServiceClickCounter serviceClickCounter;
    private final CustomerServiceSearch customerServiceSearch;
    private final CustomerEditor customerEditor;

    @GetMapping
    public String renderCustomerPage(@RequestParam(value = "serviceName", required = false) String serviceName, Model model) {

        List<String> allCategories = categoryAdminister.getAllCategories();

        model.addAttribute("serviceWithClick", findResult(getCustomerPrincipal().getId(), ServiceSearchConditionForm.of(allCategories, serviceName)));

        model.addAttribute("allCategories", allCategories);

        model.addAttribute("simpleCustomer", simpleCustomerDto());

        return "customer/customer-page";

    }

    @GetMapping("/service/edit")
    public String renderCustomerServiceEdit(@AuthenticationPrincipal CustomerPrincipal customerPrincipal,
                                            @RequestParam(value = "serviceName", required = false) String serviceName,
                                            Model model) {

        List<String> allCategories = categoryAdminister.getAllCategories();

        model.addAttribute("serviceWithClick", findResult(customerPrincipal.getId(), ServiceSearchConditionForm.of(allCategories, serviceName)));

        model.addAttribute("allCategories", allCategories);

        model.addAttribute("simpleCustomer", simpleCustomerDto());

        return "customer/customer-service-edit";
    }

    @ResponseBody
    @PostMapping("/service/delete/{serviceId}")
    public String deleteCustomerService(@PathVariable Long serviceId,
                                        @RequestParam(defaultValue = "false") Boolean isCustom,
                                        @AuthenticationPrincipal CustomerPrincipal customerPrincipal) {
        if (isCustom) customerCustomServiceAdminister.deleteCustomService(customerPrincipal.getId(), serviceId); //TODO
        else customerServiceAdminister.deleteCustomerService(customerPrincipal.getId(), serviceId);
        return "OK";
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
    public String addCustomerService(@PathVariable Long serviceId, @AuthenticationPrincipal CustomerPrincipal customerPrincipal) {
        customerServiceAdminister.addCustomerService(customerPrincipal.getId(), serviceId);
        return "OK";
    }

    @ResponseBody
    @PostMapping("/delete-service/{serviceId}")
    public String deleteCustomerService(@PathVariable Long serviceId, @AuthenticationPrincipal CustomerPrincipal customerPrincipal) {

        customerServiceAdminister.deleteCustomerService(customerPrincipal.getId(), serviceId);

        return "OK";
    }

    @ResponseBody
    @PostMapping("/add-custom")
    public String addCustomService(@ModelAttribute CustomServiceForm customServiceForm,
                                   @AuthenticationPrincipal CustomerPrincipal customerPrincipal) {
        customerCustomServiceAdminister.addCustomService(customerPrincipal.getId(), customServiceForm);
        return "OK";
    }

    @GetMapping("/edit")
    public String rendercustomerProfileEditPage(
            @AuthenticationPrincipal CustomerPrincipal customerPrincipal,
            Model model) {

        model.addAttribute("customerEditForm", CustomerEditForm.from(customerPrincipal));

        return "customer/customer-edit-page";
    }

    @PostMapping("/edit")
    public String editcustomerProfile(@Valid @ModelAttribute CustomerEditForm customerEditForm,
                                      BindingResult bindingResult,
                                      @AuthenticationPrincipal CustomerPrincipal customerPrincipal
    ) {

        if (bindingResult.hasErrors()) {
            return "customer/customer-edit-page";
        }

        customerEditor.edit(customerEditForm);

        return "redirect:/customer/" + customerPrincipal.getId();
    }

    private List<ClickServiceDto> findResult(Long customerId, ServiceSearchConditionForm serviceSearchConditionForm) {
        List<ClickServiceDto> result = new ArrayList<>();
        result.addAll(customerServiceSearch.customServicesOfCustomer(customerId, serviceSearchConditionForm.getServiceName())
                .stream()
                .map(ClickServiceDto::from)
                .collect(toList()));
        result.addAll(customerServiceSearch.servicesOfCustomer(customerId, serviceSearchConditionForm));
        return result;
    }

    private SimpleCustomerDto simpleCustomerDto() {
        CustomerPrincipal customerPrincipal = getCustomerPrincipal();
        return SimpleCustomerDto.builder()
                .nickname(customerPrincipal.getNickname())
                .customerId(customerPrincipal.getId())
                .blogUrl(customerPrincipal.getBlogUrl())
                .profileUrl(customerPrincipal.getProfileImageUrl())
                .introComment(customerPrincipal.getIntroduceComment())
                .build();
    }

    private CustomerPrincipal getCustomerPrincipal() {
        return (CustomerPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }


}
