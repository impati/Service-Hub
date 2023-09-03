package com.example.servicehub.web.controller.services;

import java.util.Optional;

import javax.validation.Valid;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.servicehub.dto.services.ServiceCommentForm;
import com.example.servicehub.dto.services.ServiceCommentUpdateForm;
import com.example.servicehub.security.authentication.CustomerPrincipal;
import com.example.servicehub.service.services.ServiceCommentsAdminister;
import com.example.servicehub.service.services.SingleServiceSearch;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/comments")
public class ServiceCommentsController {

	private final ServiceCommentsAdminister serviceCommentsAdminister;
	private final SingleServiceSearch searchSingleService;

	@PostMapping
	public String addServiceComments(
		@Valid @ModelAttribute final ServiceCommentForm serviceCommentForm,
		final BindingResult bindingResult,
		final RedirectAttributes redirectAttributes
	) {
		if (bindingResult.hasErrors()) {
			redirectAttributes.addAttribute("contentError", bindingResult.getFieldError().getDefaultMessage());
			redirectAttributes.addAttribute("hasError", true);
			return "redirect:/service/" + serviceCommentForm.getServiceId();
		}

		serviceCommentsAdminister.addServiceComment(serviceCommentForm);

		return "redirect:/service/" + serviceCommentForm.getServiceId();
	}

	@GetMapping("/edit")
	public String renderEditCommentPage(
		@ModelAttribute final ServiceCommentUpdateForm serviceCommentUpdateForm,
		@AuthenticationPrincipal final CustomerPrincipal customerPrincipal,
		final Model model
	) {
		model.addAttribute("singleServiceWithCommentsDto",
			searchSingleService.searchWithComments(
				serviceCommentUpdateForm.getServiceId(),
				Optional.ofNullable(customerPrincipal.getId())
			));

		model.addAttribute("commentContent",
			serviceCommentsAdminister.bringCommentContent(serviceCommentUpdateForm.getCommentId()));

		return "service/service-edit-page";
	}

	@PostMapping("/edit")
	public String updateComment(
		@Valid @ModelAttribute final ServiceCommentUpdateForm serviceCommentUpdateForm,
		final BindingResult bindingResult,
		@AuthenticationPrincipal final CustomerPrincipal customerPrincipal
	) {
		if (bindingResult.hasErrors()) {
			return "/service/service-edit-page";
		}

		serviceCommentUpdateForm.assignCustomer(customerPrincipal.getId());

		serviceCommentsAdminister.updateServiceComment(serviceCommentUpdateForm);

		return "redirect:/service/" + serviceCommentUpdateForm.getServiceId();
	}

	@ResponseBody
	@DeleteMapping("/delete")
	public String deleteComment(
		@ModelAttribute final ServiceCommentUpdateForm serviceCommentUpdateForm,
		@AuthenticationPrincipal final CustomerPrincipal customerPrincipal
	) {
		serviceCommentsAdminister.deleteServiceComment(
			serviceCommentUpdateForm.getCommentId(),
			customerPrincipal.getId()
		);

		return "OK";
	}
}
