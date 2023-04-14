package com.example.servicehub.service.requestService;

import com.example.servicehub.domain.requestService.RequestServiceArticle;
import com.example.servicehub.dto.requestService.RequestServiceArticleSearchCondition;
import com.example.servicehub.repository.requestService.RequestServiceArticleFinder;
import com.example.servicehub.repository.requestService.RequestServiceArticleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class DefaultRequestServiceArticleSearch implements RequestServiceArticleSearch {
    private final RequestServiceArticleFinder finder;
    private final RequestServiceArticleRepository requestServiceArticleRepository;

    @Override
    public Page<RequestServiceArticle> searchArticle(RequestServiceArticleSearchCondition condition, Pageable pageable) {
        return finder.findArticle(condition.getRequestStatus(), condition.getNickname(), pageable);
    }

    @Override
    public RequestServiceArticle searchSingleArticle(Long requestServiceArticleId) {
        return requestServiceArticleRepository.findById(requestServiceArticleId).orElseThrow(IllegalStateException::new);
    }

}
