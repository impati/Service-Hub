package com.example.servicehub.service.requestService;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.servicehub.domain.requestservice.RequestServiceArticle;
import com.example.servicehub.dto.requestService.RequestServiceArticleForm;
import com.example.servicehub.repository.requestService.RequestServiceArticleRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class RequestServiceArticleRegister {

	private final RequestServiceArticleRepository requestServiceArticleRepository;

	public Long register(
		final Long customerId,
		final String nickname,
		final RequestServiceArticleForm form
	) {
		final RequestServiceArticle article = requestServiceArticleRepository.save(RequestServiceArticle
			.builder()
			.articleTitle(form.getArticleTitle())
			.articleDescription(form.getArticleDescription())
			.serviceUrl(form.getServiceUrl())
			.logoStoreName(form.getStoreName())
			.serviceTitle(form.getServiceTitle())
			.serviceName(form.getServiceName())
			.serviceContent(form.getServiceContent())
			.nickname(nickname)
			.customerId(customerId)
			.build());
		return article.getId();
	}
}
