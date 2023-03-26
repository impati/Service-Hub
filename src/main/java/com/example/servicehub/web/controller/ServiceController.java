package com.example.servicehub.web.controller;

import com.example.servicehub.dto.PopularityServiceDto;
import com.example.servicehub.dto.ServiceSearchConditionForm;
import com.example.servicehub.dto.ServiceUpdateForm;
import com.example.servicehub.dto.ServicesRegisterForm;
import com.example.servicehub.security.authentication.CustomerPrincipal;
import com.example.servicehub.security.authentication.CustomerPrincipalUtil;
import com.example.servicehub.service.*;
import com.example.servicehub.support.MetaDataCrawler;
import com.example.servicehub.support.ServiceMetaData;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/service")
public class ServiceController {

    private final static int FIRST_PAGE = 0;
    private final CategoryAdminister categoryAdminister;
    private final ServicesRegister servicesRegister;
    private final ServiceSearch serviceSearch;
    private final MetaDataCrawler metaDataCrawler;
    private final ServiceUpdate serviceUpdate;
    private final SingleServiceSearch searchSingleService;

    @GetMapping("/registration")
    public String renderServiceRegistrationPage(@RequestParam(value = "serviceUrl", required = false) String serviceUrl,
                                                Model model) {

        ServiceMetaData serviceMetaData = metaDataCrawler.tryToGetMetaData(serviceUrl);

        model.addAttribute("serviceRegisterForm", ServicesRegisterForm.of(
                serviceMetaData.getSiteName(),
                serviceMetaData.getUrl(),
                serviceMetaData.getTitle(),
                serviceMetaData.getDescription(),
                serviceMetaData.getImage()
        ));

        model.addAttribute("categories", categoryAdminister.getAllCategories());

        return "service/registration";
    }

    @PostMapping("/registration")
    public String RegisterService(@Valid @ModelAttribute("serviceRegisterForm") ServicesRegisterForm servicesRegisterForm,
                                  BindingResult bindingResult,
                                  Model model) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("categories", categoryAdminister.getAllCategories());
            return "service/registration";
        }

        servicesRegister.registerServices(servicesRegisterForm);

        return "redirect:/";
    }

    @GetMapping("/{serviceId}/update")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public String renderServiceUpdatePage(@PathVariable Long serviceId, Model model) {

        model.addAttribute("serviceUpdateForm", ServiceUpdateForm.from(searchSingleService.search(serviceId)));

        model.addAttribute("categories", categoryAdminister.getAllCategories());

        return "service/service-update";
    }

    @PostMapping("/{serviceId}/update")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public String serviceUpdate(@PathVariable String serviceId,
                                @ModelAttribute ServiceUpdateForm serviceUpdateForm,
                                BindingResult bindingResult,
                                Model model) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("categories", categoryAdminister.getAllCategories());
            return "service/service-update";
        }

        serviceUpdate.update(serviceUpdateForm);

        return "redirect:/service/" + serviceId;
    }


    @GetMapping("/search")
    public String renderServiceSearch(@ModelAttribute ServiceSearchConditionForm serviceSearchConditionForm,
                                      @AuthenticationPrincipal CustomerPrincipal customerPrincipal,
                                      @PageableDefault Pageable pageable,
                                      Model model) {

        Page<PopularityServiceDto> searchedServices = serviceSearch.search(serviceSearchConditionForm, CustomerPrincipalUtil.getClientIdFrom(customerPrincipal), pageable);


        model.addAttribute("firstPage", FIRST_PAGE);

        model.addAttribute("currentPage", pageable.getPageNumber());

        model.addAttribute("endPage", Math.max(searchedServices.getTotalPages() - 1, 0));

        model.addAttribute("searchedServices", searchedServices.getContent());

        model.addAttribute("serviceSearchConditionForm", serviceSearchConditionForm);

        model.addAttribute("categories", categoryAdminister.getAllCategories());

        return "service/search";
    }


    @GetMapping("/{serviceId}")
    public String renderServicePage(@PathVariable Long serviceId, @AuthenticationPrincipal CustomerPrincipal customerPrincipal, Model model) {

        model.addAttribute("singleServiceWithCommentsDto", searchSingleService.searchWithComments(serviceId, CustomerPrincipalUtil.getClientIdFrom(customerPrincipal)));

        return "service/service-page";
    }


}
