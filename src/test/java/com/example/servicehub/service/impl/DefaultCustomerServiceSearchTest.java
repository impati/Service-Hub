package com.example.servicehub.service.impl;


import com.example.servicehub.config.StepsConfig;
import com.example.servicehub.config.TestJpaConfig;
import com.example.servicehub.domain.category.Category;
import com.example.servicehub.domain.services.Services;
import com.example.servicehub.dto.services.ClickServiceDto;
import com.example.servicehub.dto.services.ServiceSearchConditionForm;
import com.example.servicehub.service.customer.CustomerServiceSearch;
import com.example.servicehub.service.customer.DefaultCustomerServiceSearch;
import com.example.servicehub.steps.CategorySteps;
import com.example.servicehub.steps.CustomerServiceSteps;
import com.example.servicehub.steps.ServiceCategorySteps;
import com.example.servicehub.steps.ServicesSteps;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName(" 사용자 서비스 조회 ")
@DataJpaTest
@Import({TestJpaConfig.class, StepsConfig.class, DefaultCustomerServiceSearch.class})
class DefaultCustomerServiceSearchTest {

    @Autowired
    private CustomerServiceSearch customerServiceSearch;
    @Autowired
    private ServiceCategorySteps serviceCategorySteps;
    @Autowired
    private ServicesSteps servicesSteps;
    @Autowired
    private CategorySteps categorySteps;
    @Autowired
    private CustomerServiceSteps customerServiceSteps;

    @BeforeEach
    void setup() {

        Category it = categorySteps.create("IT");
        Category blog = categorySteps.create("BLOG");
        Category job = categorySteps.create("JOB");

        Services notion = servicesSteps.create("노션", "https://notion.so");
        Services github = servicesSteps.create("깃허브", "https://github.com");
        Services youtube = servicesSteps.create("유튜브", "https://youtube.com");
        Services jobKorea = servicesSteps.create("잡코리아", "https://jobkorea.com");

        serviceCategorySteps.create(blog, notion);
        serviceCategorySteps.create(it, notion);

        serviceCategorySteps.create(it, github);
        serviceCategorySteps.create(it, youtube);

        serviceCategorySteps.create(it, jobKorea);
        serviceCategorySteps.create(job, jobKorea);

        Long customerId = 1L;
        customerServiceSteps.createWithClick(customerId, notion, 50);
        customerServiceSteps.createWithClick(customerId, github, 10);
        customerServiceSteps.createWithClick(customerId, youtube, 2);
    }


    @Test
    @DisplayName(" 클릭 수 정렬 테스트")
    public void givenSearchCondition_whenSearchingFitTheConditionDefaultSortByClick_thenSearchedService() throws Exception {
        // given
        ServiceSearchConditionForm condition = ServiceSearchConditionForm.of(List.of("IT", "BLOG", "JOB"), null);
        // when
        List<ClickServiceDto> response = customerServiceSearch.servicesOfCustomer(1L, condition);
        // then
        assertThat(response.size())
                .isEqualTo(3);

        assertThat(response.get(0).getServiceName()).isEqualTo("노션");
        assertThat(response.get(0).getClick()).isEqualTo(50);

        assertThat(response.get(1).getServiceName()).isEqualTo("깃허브");
        assertThat(response.get(1).getClick()).isEqualTo(10);

        assertThat(response.get(2).getServiceName()).isEqualTo("유튜브");
        assertThat(response.get(2).getClick()).isEqualTo(2);

    }


    @Test
    @DisplayName("카테고리 테스트")
    public void givenCategoryCondition_whenSearchingFitTheConditionDefaultSortByClick_thenSearchedService() throws Exception {
        // given
        ServiceSearchConditionForm condition = ServiceSearchConditionForm.of(List.of("BLOG"), null);
        // when
        List<ClickServiceDto> response = customerServiceSearch.servicesOfCustomer(1L, condition);
        // then
        assertThat(response.size())
                .isEqualTo(1);

        assertThat(response.get(0).getServiceName()).isEqualTo("노션");

    }

    @Test
    @DisplayName("서비스 이름 검색")
    public void given_when_then() throws Exception {
        // given
        ServiceSearchConditionForm condition = ServiceSearchConditionForm.of(null, "브");
        // when
        List<ClickServiceDto> response = customerServiceSearch.servicesOfCustomer(1L, condition);
        // then
        assertThat(response.size())
                .isEqualTo(2);

        assertThat(response.get(0).getServiceName()).isEqualTo("깃허브");
        assertThat(response.get(0).getClick()).isEqualTo(10);

        assertThat(response.get(1).getServiceName()).isEqualTo("유튜브");
        assertThat(response.get(1).getClick()).isEqualTo(2);
    }


}