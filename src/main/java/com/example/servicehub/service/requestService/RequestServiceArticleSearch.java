package com.example.servicehub.service.requestService;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.servicehub.domain.requestservice.RequestServiceArticle;
import com.example.servicehub.dto.requestService.RequestServiceArticleSearchCondition;

public interface RequestServiceArticleSearch {

	Page<RequestServiceArticle> searchArticle(
		final RequestServiceArticleSearchCondition condition,
		final Pageable pageable
	);

	RequestServiceArticle searchSingleArticle(final Long requestServiceArticleId);
}
