package com.example.servicehub.web;

import com.example.servicehub.domain.Client;
import com.example.servicehub.dto.PopularityServiceDto;
import com.example.servicehub.dto.ServiceSearchConditionForm;
import com.example.servicehub.dto.ServicesRegisterForm;
import com.example.servicehub.security.authentication.ClientContext;
import com.example.servicehub.service.CategoryAdminister;
import com.example.servicehub.service.ServiceSearch;
import com.example.servicehub.service.ServicesRegister;
import com.example.servicehub.support.MetaDataCrawler;
import com.example.servicehub.web.util.ClientIdGetter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/service")
public class ServiceController {

    private final CategoryAdminister categoryAdminister;
    private final ServicesRegister servicesRegister;
    private final ServiceSearch serviceSearch;
    private final MetaDataCrawler metaDataCrawler;


    @GetMapping("/registration")
    public String renderServiceRegistrationPage(@RequestParam(value = "serviceUrl",required = false) String serviceUrl,
                                                    Model model){

        model.addAttribute("metaData",metaDataCrawler.tryToGetMetaData(serviceUrl));

        model.addAttribute("serviceRegisterForm",new ServicesRegisterForm());

        model.addAttribute("categories",categoryAdminister.getAllCategories());

        return "service/registration";
    }

    @PostMapping("/registration")
    public String RegisterService(@ModelAttribute ServicesRegisterForm servicesRegisterForm){

        servicesRegister.registerServices(servicesRegisterForm);

        return "redirect:/";
    }

    @GetMapping("/search")
    public String renderServiceSearch(@ModelAttribute ServiceSearchConditionForm serviceSearchConditionForm,
                                      Authentication authentication ,
                                      Model model){


        Page<PopularityServiceDto> searchedServices = serviceSearch.search(serviceSearchConditionForm,returnClientId(authentication));

        model.addAttribute("searchedServices",searchedServices.getContent());

        model.addAttribute("serviceSearchConditionForm",serviceSearchConditionForm);

        model.addAttribute("categories",categoryAdminister.getAllCategories());

        return "service/search";
    }

    private Optional<Long> returnClientId(Authentication authentication){
        if(authentication == null) return Optional.empty();
        return Optional.of(authentication.getPrincipal())
                .map(type->(ClientContext)type)
                .map(ClientContext::getClient)
                .map(Client::getId);
    }

    @GetMapping("/{serviceId}")
    public String renderServicePage(@PathVariable Long serviceId , UsernamePasswordAuthenticationToken authenticationToken, Model model){

        model.addAttribute("singleServiceWithCommentsDto"
        ,serviceSearch.searchSingleService(serviceId, Optional.ofNullable(ClientIdGetter.getIdFrom(authenticationToken))));

        return "service/service-page";
}



}
