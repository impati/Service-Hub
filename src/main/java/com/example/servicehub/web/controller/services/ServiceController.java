package com.example.servicehub.web.controller.services;

import javax.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.servicehub.dto.services.PopularityServiceDto;
import com.example.servicehub.dto.services.ServiceSearchConditionForm;
import com.example.servicehub.dto.services.ServiceUpdateForm;
import com.example.servicehub.dto.services.ServicesRegisterForm;
import com.example.servicehub.security.authentication.CustomerPrincipal;
import com.example.servicehub.security.authentication.CustomerPrincipalUtil;
import com.example.servicehub.service.category.CategoryAdminister;
import com.example.servicehub.service.services.ServiceSearch;
import com.example.servicehub.service.services.ServiceUpdate;
import com.example.servicehub.service.services.ServicesRegister;
import com.example.servicehub.service.services.SingleServiceSearch;
import com.example.servicehub.support.crawl.MetaDataCrawler;
import com.example.servicehub.support.crawl.ServiceMetaData;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/service")
public class ServiceController {

	private static final int FIRST_PAGE = 0;
	private final CategoryAdminister categoryAdminister;
	private final ServicesRegister servicesRegister;
	private final ServiceSearch serviceSearch;
	private final MetaDataCrawler metaDataCrawler;
	private final ServiceUpdate serviceUpdate;
	private final SingleServiceSearch searchSingleService;

	@GetMapping("/registration")
	public String renderServiceRegistrationPage(
		@RequestParam(value = "serviceUrl", required = false) final String serviceUrl,
		final Model model
	) {
		final ServiceMetaData serviceMetaData = metaDataCrawler.tryToGetMetaData(serviceUrl);

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
	public String RegisterService(
		@Valid @ModelAttribute("serviceRegisterForm") final ServicesRegisterForm servicesRegisterForm,
		final BindingResult bindingResult,
		final Model model
	) {
		if (bindingResult.hasErrors()) {
			model.addAttribute("categories", categoryAdminister.getAllCategories());
			return "service/registration";
		}

		servicesRegister.registerServices(servicesRegisterForm);

		return "redirect:/";
	}

	@GetMapping("/{serviceId}/update")
	@PreAuthorize("hasAnyRole('ADMIN')")
	public String renderServiceUpdatePage(@PathVariable final Long serviceId, final Model model) {
		model.addAttribute("serviceUpdateForm", ServiceUpdateForm.from(searchSingleService.search(serviceId)));

		model.addAttribute("categories", categoryAdminister.getAllCategories());

		return "service/service-update";
	}

	@PostMapping("/{serviceId}/update")
	@PreAuthorize("hasAnyRole('ADMIN')")
	public String serviceUpdate(
		@PathVariable final String serviceId,
		@ModelAttribute final ServiceUpdateForm serviceUpdateForm,
		final BindingResult bindingResult,
		final Model model
	) {
		if (bindingResult.hasErrors()) {
			model.addAttribute("categories", categoryAdminister.getAllCategories());
			return "service/service-update";
		}

		serviceUpdate.update(serviceUpdateForm);

		return "redirect:/service/" + serviceId;
	}

	@GetMapping("/search")
	public String renderServiceSearch(
		@ModelAttribute final ServiceSearchConditionForm serviceSearchConditionForm,
		@AuthenticationPrincipal final CustomerPrincipal customerPrincipal,
		@PageableDefault final Pageable pageable,
		final Model model
	) {

		final Page<PopularityServiceDto> searchedServices = serviceSearch.search(serviceSearchConditionForm,
			CustomerPrincipalUtil.getCustomerIdFrom(customerPrincipal), pageable);

		model.addAttribute("firstPage", FIRST_PAGE);

		model.addAttribute("currentPage", pageable.getPageNumber());

		model.addAttribute("endPage", Math.max(searchedServices.getTotalPages() - 1, 0));

		model.addAttribute("searchedServices", searchedServices.getContent());

		model.addAttribute("serviceSearchConditionForm", serviceSearchConditionForm);

		model.addAttribute("categories", categoryAdminister.getAllCategories());

		return "service/search";
	}

	@GetMapping("/{serviceId}")
	public String renderServicePage(
		@PathVariable final Long serviceId,
		@AuthenticationPrincipal final CustomerPrincipal customerPrincipal,
		final Model model
	) {
		model.addAttribute("singleServiceWithCommentsDto", searchSingleService.searchWithComments(serviceId,
			CustomerPrincipalUtil.getCustomerIdFrom(customerPrincipal)));

		return "service/service-page";
	}
}
