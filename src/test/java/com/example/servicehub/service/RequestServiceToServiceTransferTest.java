package com.example.servicehub.service;

import com.example.servicehub.config.TestJpaConfig;
import com.example.servicehub.domain.requestService.RequestServiceArticle;
import com.example.servicehub.domain.requestService.RequestStatus;
import com.example.servicehub.domain.services.Services;
import com.example.servicehub.repository.category.CategoryRepository;
import com.example.servicehub.repository.requestService.RequestServiceArticleRepository;
import com.example.servicehub.repository.services.ServicesRepository;
import com.example.servicehub.service.requestService.RequestServiceToServiceTransfer;
import com.example.servicehub.steps.CategorySteps;
import com.example.servicehub.steps.RequestServiceArticleSteps;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
@SpringBootTest(properties = "spring.main.allow-bean-definition-overriding=true", classes = TestJpaConfig.class)
class RequestServiceToServiceTransferTest {

    @Autowired
    private RequestServiceToServiceTransfer requestServiceToServiceTransfer;

    @Autowired
    private RequestServiceArticleRepository requestServiceArticleRepository;

    @Autowired
    private ServicesRepository servicesRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    private RequestServiceArticleSteps requestServiceArticleSteps;
    private CategorySteps categorySteps;

    @BeforeEach
    void setup() {
        categorySteps = new CategorySteps(categoryRepository);
        requestServiceArticleSteps = new RequestServiceArticleSteps(requestServiceArticleRepository);

    }

    @Test
    @DisplayName("요청된 서비스를 서비스로 등록 테스트")
    public void givenRequestService_whenRegisteringServices_thenRegisterServices() throws Exception {
        // given
        categorySteps.create("AI");

        RequestServiceArticle requestServiceArticle = requestServiceArticleSteps.create();

        // when
        requestServiceToServiceTransfer.registerRequestedServiceAsService(
                requestServiceArticle.getId(), List.of("AI"), RequestStatus.COMPLETE);

        // then
        assertThat(requestServiceArticle.getRequestStatus()).isEqualTo(RequestStatus.COMPLETE);
        Services services = servicesRepository.findByServiceUrl(requestServiceArticle.getServiceUrl()).get();

        assertThat(services.getServiceName()).isEqualTo(requestServiceArticle.getServiceName());
        assertThat(services.getServiceUrl()).isEqualTo(requestServiceArticle.getServiceUrl());
        assertThat(services.getContent()).isEqualTo(requestServiceArticle.getServiceContent());
        assertThat(services.getTitle()).isEqualTo(requestServiceArticle.getServiceTitle());
        assertThat(services.getLogoStoreName()).isEqualTo(requestServiceArticle.getLogoStoreName());
    }

    @Test
    @DisplayName("요청된 서비스를 보류")
    public void givenRequestService_whenDeferServices_thenDeferRequestServices() throws Exception {
        // given
        RequestServiceArticle requestServiceArticle = requestServiceArticleSteps.create();

        // when
        requestServiceToServiceTransfer.registerRequestedServiceAsService(
                requestServiceArticle.getId(), null, RequestStatus.DEFER);

        // then
        assertThat(servicesRepository.findByServiceUrl(requestServiceArticle.getServiceUrl()))
                .isEmpty();

        assertThat(requestServiceArticle.getRequestStatus()).isEqualTo(RequestStatus.DEFER);

    }

    @Test
    @DisplayName("요청된 서비스를 거절")
    public void givenRequestService_whenRejectServices_thenRejectRequestServices() throws Exception {
        // given
        RequestServiceArticle requestServiceArticle = requestServiceArticleSteps.create();

        // when
        requestServiceToServiceTransfer.registerRequestedServiceAsService(
                requestServiceArticle.getId(), null, RequestStatus.FAIL);


        // then
        assertThat(servicesRepository.findByServiceUrl(requestServiceArticle.getServiceUrl()))
                .isEmpty();

        assertThat(requestServiceArticle.getRequestStatus()).isEqualTo(RequestStatus.FAIL);
    }


}