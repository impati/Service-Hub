package com.example.servicehub.web;

import com.example.servicehub.dto.PopularityServiceDto;
import com.example.servicehub.dto.ServiceSearchConditionForm;
import com.example.servicehub.dto.ServicesRegisterForm;
import com.example.servicehub.dto.SingleServiceWithCommentsDto;
import com.example.servicehub.service.CategoryAdminister;
import com.example.servicehub.service.ServiceSearch;
import com.example.servicehub.service.ServicesRegister;
import com.example.servicehub.support.MetaDataCrawler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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
    public String renderServiceRegistrationPage(
            @RequestParam(value = "servicesUrl",required = false) String servicesUrl,
                                                    Model model){
        model.addAttribute("metaData",metaDataCrawler.tryToGetMetaData(servicesUrl));
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
    public String renderServiceSearch(@ModelAttribute ServiceSearchConditionForm serviceSearchConditionForm, Model model){

        Page<PopularityServiceDto> searchedServices = serviceSearch.search(serviceSearchConditionForm);
        model.addAttribute("searchedServices",searchedServices.getContent());
        model.addAttribute("serviceSearchConditionForm",serviceSearchConditionForm);
        model.addAttribute("categories",categoryAdminister.getAllCategories());

        return "service/search";
    }


    @GetMapping("/{serviceId}")
    public String renderServicePage(@PathVariable Long serviceId , Model model){
        model.addAttribute("singleServiceWithCommentsDto"
                ,serviceSearch.searchSingleService(serviceId, 1L));//  TODO : 사용자 처리

        return "service/service-page";
    }



}
