package com.example.servicehub.web.controller.customer;

import static java.util.stream.Collectors.*;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

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
	public String renderCustomerPage(
		@RequestParam(value = "serviceName", required = false) final String serviceName,
		final Model model
	) {
		final List<String> allCategories = categoryAdminister.getAllCategories();

		model.addAttribute("serviceWithClick",
			findResult(getCustomerPrincipal().getId(), ServiceSearchConditionForm.of(allCategories, serviceName)));

		model.addAttribute("allCategories", allCategories);

		model.addAttribute("simpleCustomer", simpleCustomerDto());

		return "customer/customer-page";
	}

	@GetMapping("/service/edit")
	public String renderCustomerServiceEdit(
		@AuthenticationPrincipal final CustomerPrincipal customerPrincipal,
		@RequestParam(value = "serviceName", required = false) final String serviceName,
		final Model model
	) {
		final List<String> allCategories = categoryAdminister.getAllCategories();

		model.addAttribute("serviceWithClick",
			findResult(customerPrincipal.getId(), ServiceSearchConditionForm.of(allCategories, serviceName)));

		model.addAttribute("allCategories", allCategories);

		model.addAttribute("simpleCustomer", simpleCustomerDto());

		return "customer/customer-service-edit";
	}

	@ResponseBody
	@PostMapping("/service/delete/{serviceId}")
	public String deleteCustomerService(
		@PathVariable final Long serviceId,
		@RequestParam(defaultValue = "false") final Boolean isCustom,
		@AuthenticationPrincipal final CustomerPrincipal customerPrincipal
	) {
		if (isCustom(isCustom)) {
			customerCustomServiceAdminister.deleteCustomService(customerPrincipal.getId(), serviceId);
		}

		if (!isCustom(isCustom)) {
			customerServiceAdminister.deleteCustomerService(customerPrincipal.getId(), serviceId);
		}

		return "OK";
	}

	@GetMapping("/click")
	public String clickService(
		@RequestParam final Long serviceId,
		@RequestParam(defaultValue = "false") final Boolean isCustom,
		@AuthenticationPrincipal final CustomerPrincipal customerPrincipal
	) {
		String serviceUrl = "";
		if (isCustom(isCustom)) {
			serviceUrl = customerCustomServiceAdminister.countClickAndReturnUrl(customerPrincipal.getId(), serviceId);
		}

		if (!isCustom(isCustom)) {
			serviceUrl = serviceClickCounter.countClickAndReturnUrl(customerPrincipal.getId(), serviceId);
		}

		return "redirect:" + serviceUrl;
	}

	@ResponseBody
	@PostMapping("/add-service/{serviceId}")
	public String addCustomerService(
		@PathVariable final Long serviceId,
		@AuthenticationPrincipal final CustomerPrincipal customerPrincipal
	) {
		customerServiceAdminister.addCustomerService(customerPrincipal.getId(), serviceId);

		return "OK";
	}

	@ResponseBody
	@PostMapping("/delete-service/{serviceId}")
	public String deleteCustomerService(
		@PathVariable final Long serviceId,
		@AuthenticationPrincipal final CustomerPrincipal customerPrincipal
	) {
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
		@AuthenticationPrincipal final CustomerPrincipal customerPrincipal,
		final Model model
	) {
		model.addAttribute("customerEditForm", CustomerEditForm.from(customerPrincipal));

		return "customer/customer-edit-page";
	}

	@PostMapping("/edit")
	public String editcustomerProfile(
		@Valid @ModelAttribute final CustomerEditForm customerEditForm,
		final BindingResult bindingResult,
		@AuthenticationPrincipal final CustomerPrincipal customerPrincipal
	) {

		if (bindingResult.hasErrors()) {
			return "customer/customer-edit-page";
		}

		customerEditor.edit(customerEditForm);

		return "redirect:/customer/" + customerPrincipal.getId();
	}

	private List<ClickServiceDto> findResult(
		final Long customerId,
		final ServiceSearchConditionForm serviceSearchConditionForm
	) {
		final List<ClickServiceDto> result = new ArrayList<>();
		result.addAll(
			customerServiceSearch.customServicesOfCustomer(customerId, serviceSearchConditionForm.getServiceName())
				.stream()
				.map(ClickServiceDto::from)
				.collect(toList()));
		result.addAll(customerServiceSearch.servicesOfCustomer(customerId, serviceSearchConditionForm));
		return result;
	}

	private SimpleCustomerDto simpleCustomerDto() {
		final CustomerPrincipal customerPrincipal = getCustomerPrincipal();
		return SimpleCustomerDto.builder()
			.nickname(customerPrincipal.getNickname())
			.customerId(customerPrincipal.getId())
			.blogUrl(customerPrincipal.getBlogUrl())
			.profileUrl(customerPrincipal.getProfileImageUrl())
			.introComment(customerPrincipal.getIntroduceComment())
			.build();
	}

	private CustomerPrincipal getCustomerPrincipal() {
		return (CustomerPrincipal)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	}

	private static boolean isCustom(final Boolean isCustom) {
		return isCustom;
	}
}
