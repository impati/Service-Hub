package com.example.servicehub.web;

import com.example.servicehub.dto.PopularityServiceDto;
import com.example.servicehub.dto.ServiceCommentForm;
import com.example.servicehub.dto.ServiceSearchConditionForm;
import com.example.servicehub.dto.ServicesRegisterForm;
import com.example.servicehub.security.authentication.ClientPrincipal;
import com.example.servicehub.security.authentication.ClientPrincipalUtil;
import com.example.servicehub.service.CategoryAdminister;
import com.example.servicehub.service.ServiceSearch;
import com.example.servicehub.service.ServicesRegister;
import com.example.servicehub.support.MetaDataCrawler;
import com.example.servicehub.support.ServiceMetaData;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Optional;

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


    @GetMapping("/registration")
    public String renderServiceRegistrationPage(@RequestParam(value = "serviceUrl",required = false) String serviceUrl,
                                                    Model model){

        ServiceMetaData serviceMetaData = metaDataCrawler.tryToGetMetaData(serviceUrl);

        model.addAttribute("serviceRegisterForm",ServicesRegisterForm.of(
                serviceMetaData.getSiteName(),
                serviceMetaData.getUrl(),
                serviceMetaData.getTitle(),
                serviceMetaData.getDescription(),
                serviceMetaData.getImage()
        ));

        model.addAttribute("categories",categoryAdminister.getAllCategories());

        return "service/registration";
    }

    @PostMapping("/registration")
    public String RegisterService(@Valid @ModelAttribute("serviceRegisterForm")ServicesRegisterForm servicesRegisterForm,
                                  BindingResult bindingResult,
                                  Model model){

        if(bindingResult.hasErrors()){
            model.addAttribute("categories",categoryAdminister.getAllCategories());
            return "service/registration";
        }

        servicesRegister.registerServices(servicesRegisterForm);

        return "redirect:/";
    }

    @GetMapping("/search")
    public String renderServiceSearch(@ModelAttribute ServiceSearchConditionForm serviceSearchConditionForm,
                                      @AuthenticationPrincipal ClientPrincipal clientPrincipal ,
                                      @PageableDefault Pageable pageable,
                                      Model model){

        Page<PopularityServiceDto> searchedServices = serviceSearch.search(serviceSearchConditionForm, ClientPrincipalUtil.getClientIdFrom(clientPrincipal),pageable);


        model.addAttribute("firstPage",FIRST_PAGE);

        model.addAttribute("currentPage",pageable.getPageNumber());

        model.addAttribute("endPage",searchedServices.getTotalPages() - 1);

        model.addAttribute("searchedServices",searchedServices.getContent());

        model.addAttribute("serviceSearchConditionForm",serviceSearchConditionForm);

        model.addAttribute("categories",categoryAdminister.getAllCategories());

        return "service/search";
    }


    @GetMapping("/{serviceId}")
    public String renderServicePage(@PathVariable Long serviceId , @AuthenticationPrincipal ClientPrincipal clientPrincipal, Model model){

        model.addAttribute("singleServiceWithCommentsDto",serviceSearch.searchSingleService(serviceId, ClientPrincipalUtil.getClientIdFrom(clientPrincipal)));

        return "service/service-page";
    }



}
