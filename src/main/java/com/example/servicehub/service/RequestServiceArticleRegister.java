package com.example.servicehub.service;

import com.example.servicehub.domain.RequestServiceArticle;
import com.example.servicehub.dto.RequestServiceArticleForm;
import com.example.servicehub.repository.RequestServiceArticleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class RequestServiceArticleRegister {

    private final RequestServiceArticleRepository requestServiceArticleRepository;

    public Long register(Long customerId, String nickname, RequestServiceArticleForm form) {
        RequestServiceArticle article = requestServiceArticleRepository.save(RequestServiceArticle
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
