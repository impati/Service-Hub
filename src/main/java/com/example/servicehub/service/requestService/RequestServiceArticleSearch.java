package com.example.servicehub.service.requestService;

import com.example.servicehub.domain.requestService.RequestServiceArticle;
import com.example.servicehub.dto.requestService.RequestServiceArticleSearchCondition;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface RequestServiceArticleSearch {
    Page<RequestServiceArticle> searchArticle(RequestServiceArticleSearchCondition condition, Pageable pageable);

    RequestServiceArticle searchSingleArticle(Long requestServiceArticleId);
}
