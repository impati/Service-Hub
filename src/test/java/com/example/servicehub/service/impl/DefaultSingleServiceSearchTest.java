package com.example.servicehub.service.impl;

import com.example.servicehub.config.StepsConfig;
import com.example.servicehub.config.TestJpaConfig;
import com.example.servicehub.domain.category.Category;
import com.example.servicehub.domain.services.Services;
import com.example.servicehub.dto.services.SingleServiceWithCommentsDto;
import com.example.servicehub.service.services.DefaultSingleServiceSearch;
import com.example.servicehub.service.services.ServiceCommentsAdministerImpl;
import com.example.servicehub.service.services.SingleServiceSearch;
import com.example.servicehub.steps.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
@Import({TestJpaConfig.class, StepsConfig.class, DefaultSingleServiceSearch.class, ServiceCommentsAdministerImpl.class})
class DefaultSingleServiceSearchTest {
    @Autowired
    private SingleServiceSearch singleServiceSearch;
    @Autowired
    private ServicesSteps servicesSteps;
    @Autowired
    private ServiceCategorySteps serviceCategorySteps;
    @Autowired
    private CustomerServiceSteps customerServiceSteps;
    @Autowired
    private ServiceCommentsSteps serviceCommentsSteps;
    @Autowired
    private CategorySteps categorySteps;

    @Test
    @DisplayName("단일 서비스 조회 - 댓글이 없는 서비스 정보 조회")
    public void givenServiceId_whenSearchingServiceInformation_thenReturnServiceInformation() throws Exception {
        // given
        Long customerId = 1L;
        Services services = servicesSteps.create();
        serviceCategorySteps.create("IT", services);
        customerServiceSteps.create(customerId, services);
        // when
        SingleServiceWithCommentsDto singleServiceWithCommentsDto = singleServiceSearch.searchWithComments(services.getId(), Optional.of(customerId));
        // then
        assertThat(singleServiceWithCommentsDto.getComments().size()).isEqualTo(0);
    }

    @Test
    @DisplayName("단일 서비스 조회 - 사용자가 서비스 소유 체크")
    public void givenServiceIdAndcustomerId_whenSearchingService_thenReturnsWhetherThecustomerOwns() throws Exception {
        // given
        Long customerId = 1L;
        Services github = servicesSteps.create("깃허브", "https://github.com");
        Services notion = servicesSteps.create("노션", "https://notion.so");
        Category category = categorySteps.create("IT");
        serviceCategorySteps.create(category, github);
        serviceCategorySteps.create(category, notion);
        customerServiceSteps.create(customerId, github);
        // when
        SingleServiceWithCommentsDto possessSingleServiceWithCommentsDto = singleServiceSearch.searchWithComments(github.getId(), Optional.of(customerId));
        SingleServiceWithCommentsDto nonPossessSingleServiceWithCommentsDto = singleServiceSearch.searchWithComments(notion.getId(), Optional.of(customerId));
        // then
        assertThat(possessSingleServiceWithCommentsDto.isPossess()).isTrue();
        assertThat(nonPossessSingleServiceWithCommentsDto.isPossess()).isFalse();
    }

    @Test
    @DisplayName("단일 서비스 조회 - 댓글과 같이")
    public void givenServiceIdAndcustomerID_whenSearching_thenReturnServiceWithComments() throws Exception {
        // given
        Long customerId = 1L;
        Services services = servicesSteps.create();
        serviceCategorySteps.create("IT", services);
        customerServiceSteps.create(customerId, services);
        serviceCommentsSteps.create(customerId, services);
        // when
        SingleServiceWithCommentsDto response = singleServiceSearch.searchWithComments(services.getId(), Optional.of(customerId));
        // then
        assertThat(response.isPossess()).isTrue();
        assertThat(response.getComments().size())
                .isEqualTo(1);
    }

    @Test
    @DisplayName("단일 서비스 조회 - 인증된 사용자가 아닐 경우")
    public void givenUnAuthenticationUser_whenSearchingServicePage_thenReturnServiceWithComments() throws Exception {
        // given
        Long customerId = 1L;
        Services services = servicesSteps.create();
        serviceCategorySteps.create("IT", services);
        customerServiceSteps.create(customerId, services);
        serviceCommentsSteps.create(customerId, services);
        // when
        SingleServiceWithCommentsDto response = singleServiceSearch.searchWithComments(services.getId(), Optional.ofNullable(null));
        // then
        assertThat(response.isPossess()).isFalse();
        assertThat(response.getComments().size())
                .isEqualTo(1);
    }

    @Test
    @DisplayName("단일 서비스 조회 - 수정 페이지")
    public void givenServiceID_whenSearchFetchCategories_thenServiceWithCategories() throws Exception {
        // given
        Services services = servicesSteps.create();
        serviceCategorySteps.create("IT", services);
        // when
        Services search = singleServiceSearch.search(services.getId());
        // then
        assertThat(search.getServiceCategories().size())
                .isEqualTo(1);
    }
}