package com.example.servicehub.web.controller.requestService;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.servicehub.domain.requestservice.RequestServiceArticle;
import com.example.servicehub.domain.requestservice.RequestStatus;
import com.example.servicehub.dto.requestService.RequestServiceArticleForm;
import com.example.servicehub.dto.requestService.RequestServiceArticleSearchCondition;
import com.example.servicehub.security.authentication.CustomerPrincipal;
import com.example.servicehub.service.category.CategoryAdminister;
import com.example.servicehub.service.requestService.RequestServiceArticleRegister;
import com.example.servicehub.service.requestService.RequestServiceArticleSearch;
import com.example.servicehub.service.requestService.RequestServiceToServiceTransfer;
import com.example.servicehub.web.dto.requestService.RequestedServiceResponse;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/requested-service")
@RequiredArgsConstructor
public class RequestServiceArticleController {

	private final RequestServiceArticleSearch requestServiceArticleSearch;
	private final RequestServiceToServiceTransfer requestServiceToServiceTransfer;
	private final RequestServiceArticleRegister requestServiceArticleRegister;
	private final CategoryAdminister categoryAdminister;

	@GetMapping
	public String renderRequestServiceArticles(
		@ModelAttribute(name = "condition") final RequestServiceArticleSearchCondition condition,
		@PageableDefault final Pageable pageable,
		final Model model
	) {
		Page<RequestServiceArticle> response = requestServiceArticleSearch.searchArticle(condition, pageable);

		model.addAttribute("articles", response.getContent());

		return "requested-service/articles";
	}

	@GetMapping("/{articleId}")
	public String renderRequestServiceArticle(
		@PathVariable final Long articleId,
		final Model model
	) {
		RequestedServiceResponse response =
			RequestedServiceResponse.fromEntity(requestServiceArticleSearch.searchSingleArticle(articleId));

		model.addAttribute("article", response);
		model.addAttribute("categoryNames", categoryAdminister.getAllCategories());

		return "requested-service/article";
	}

	@PostMapping("/{articleId}")
	public String saveService(
		@PathVariable final Long articleId,
		@RequestParam(required = false) final List<String> categoryNames,
		@RequestParam(name = "status") final RequestStatus requestStatus
	) {
		requestServiceToServiceTransfer.registerRequestedServiceAsService(articleId, categoryNames, requestStatus);

		return "redirect:/requested-service/" + articleId;
	}

	@GetMapping("/registration")
	public String renderRegistrationPage(final Model model) {
		model.addAttribute("requestServiceArticleForm", new RequestServiceArticleForm());

		return "requested-service/article-registration";
	}

	@PostMapping("/registration")
	public String register(
		@ModelAttribute final RequestServiceArticleForm form,
		@AuthenticationPrincipal final CustomerPrincipal customerPrincipal
	) {
		final Long articleId = requestServiceArticleRegister.register(customerPrincipal.getId(),
			customerPrincipal.getNickname(), form);
		
		return "redirect:/requested-service/" + articleId;
	}
}
