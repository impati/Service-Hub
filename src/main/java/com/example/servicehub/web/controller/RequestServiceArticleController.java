package com.example.servicehub.web.controller;

import com.example.servicehub.domain.RequestServiceArticle;
import com.example.servicehub.domain.RequestStatus;
import com.example.servicehub.dto.RequestServiceArticleForm;
import com.example.servicehub.dto.RequestServiceArticleSearchCondition;
import com.example.servicehub.security.authentication.CustomerPrincipal;
import com.example.servicehub.service.CategoryAdminister;
import com.example.servicehub.service.RequestServiceArticleRegister;
import com.example.servicehub.service.RequestServiceArticleSearch;
import com.example.servicehub.service.RequestServiceToServiceTransfer;
import com.example.servicehub.web.dto.RequestedServiceResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
            @ModelAttribute(name = "condition") RequestServiceArticleSearchCondition condition,
            @PageableDefault Pageable pageable,
            Model model) {

        Page<RequestServiceArticle> response = requestServiceArticleSearch.searchArticle(condition, pageable);
        model.addAttribute("articles", response.getContent());

        return "requested-service/articles";
    }

    @GetMapping("/{articleId}")
    public String renderRequestServiceArticle(@PathVariable Long articleId,
                                              Model model) {

        RequestedServiceResponse response =
                RequestedServiceResponse.fromEntity(requestServiceArticleSearch.searchSingleArticle(articleId));

        model.addAttribute("article", response);
        model.addAttribute("categoryNames", categoryAdminister.getAllCategories());

        return "requested-service/article";
    }

    @PostMapping("/{articleId}")
    public String saveService(@PathVariable Long articleId,
                              @RequestParam(required = false) List<String> categoryNames,
                              @RequestParam(name = "status") RequestStatus requestStatus) {
        requestServiceToServiceTransfer.registerRequestedServiceAsService(articleId, categoryNames, requestStatus);
        return "redirect:/requested-service/" + articleId;
    }

    @GetMapping("/registration")
    public String renderRegistrationPage(Model model) {
        model.addAttribute("requestServiceArticleForm", new RequestServiceArticleForm());
        return "requested-service/article-registration";
    }

    @PostMapping("/registration")
    public String register(@ModelAttribute RequestServiceArticleForm form,
                           @AuthenticationPrincipal CustomerPrincipal customerPrincipal) {
        Long articleId = requestServiceArticleRegister.register(customerPrincipal.getId(), customerPrincipal.getNickname(), form);
        return "redirect:/requested-service/" + articleId;
    }

}
