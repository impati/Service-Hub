package com.example.servicehub.service;

import com.example.servicehub.domain.RequestServiceArticle;
import com.example.servicehub.dto.RequestServiceArticleSearchCondition;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface RequestServiceArticleSearch {
    Page<RequestServiceArticle> searchArticle(RequestServiceArticleSearchCondition condition, Pageable pageable);

    RequestServiceArticle searchSingleArticle(Long requestServiceArticleId);
}
