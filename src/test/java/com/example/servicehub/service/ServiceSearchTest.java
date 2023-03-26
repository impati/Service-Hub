package com.example.servicehub.service;

import com.example.servicehub.config.StepsConfig;
import com.example.servicehub.config.TestJpaConfig;
import com.example.servicehub.domain.Category;
import com.example.servicehub.domain.Services;
import com.example.servicehub.dto.PopularityServiceDto;
import com.example.servicehub.dto.ServiceSearchConditionForm;
import com.example.servicehub.service.impl.ServiceSearchImpl;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("서비스 검색 테스트")
@DataJpaTest
@Import({TestJpaConfig.class, StepsConfig.class, ServiceSearchImpl.class})
class ServiceSearchTest {

    @Autowired
    private ServiceSearch serviceSearch;
    @Autowired
    private ServicesSteps servicesSteps;
    @Autowired
    private CategorySteps categorySteps;
    @Autowired
    private ServiceCategorySteps serviceCategorySteps;
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
        serviceCategorySteps.create(blog, github);
        serviceCategorySteps.create(it, youtube);
        serviceCategorySteps.create(job, jobKorea);

    }

    @Test
    @DisplayName("카테고리로 검색 - 단일")
    public void givenCategory_whenSearchingService_thenReturnFitTheConditionServices() throws Exception {
        // given
        ServiceSearchConditionForm serviceSearchConditionForm =
                ServiceSearchConditionForm.of(List.of("IT"), null);
        // when
        Page<PopularityServiceDto> response = serviceSearch.search(serviceSearchConditionForm, Optional.empty(), PageRequest.of(0, 10));
        // then
        assertThat(response.getContent().size()).isEqualTo(3);
        assertThat(response.getContent().stream().map(PopularityServiceDto::getServiceName))
                .containsAnyOf("노션", "깃허브", "유튜브");
    }

    @Test
    @DisplayName("카테고리로 검색 - 멀티")
    public void givenCategories_whenSearchingService_thenReturnFitTheConditionServices() throws Exception {
        // given
        ServiceSearchConditionForm serviceSearchConditionForm =
                ServiceSearchConditionForm.of(List.of("JOB", "BLOG"), null);
        // when
        Page<PopularityServiceDto> response = serviceSearch.search(serviceSearchConditionForm, Optional.empty(), PageRequest.of(0, 10));
        // then
        assertThat(response.getContent().size()).isEqualTo(3);
        assertThat(response.getContent().stream().map(PopularityServiceDto::getServiceName))
                .containsAnyOf("잡코리아", "노션", "깃허브");
    }

    @Test
    @DisplayName("서비스 이름 검색 - 정확히 입력했을 때")
    public void givenServiceName_whenExactMatchSearchingService_thenReturnFitTheConditionServices() throws Exception {
        // given
        ServiceSearchConditionForm serviceSearchConditionForm =
                ServiceSearchConditionForm.of(null, "깃허브");
        // when
        Page<PopularityServiceDto> response = serviceSearch.search(serviceSearchConditionForm, Optional.empty(), PageRequest.of(0, 10));
        // then
        assertThat(response.getContent().size()).isEqualTo(1);
        assertThat(response.getContent().stream().map(PopularityServiceDto::getServiceName))
                .containsAnyOf("깃허브");
    }

    @Test
    @DisplayName("서비스 이름 검색 - 일부 입력했을 때")
    public void givenServiceName_whenLikeMatchSearchingService_thenReturnFitTheConditionServices() throws Exception {
        // given
        ServiceSearchConditionForm serviceSearchConditionForm =
                ServiceSearchConditionForm.of(null, "브");
        // when
        Page<PopularityServiceDto> response = serviceSearch.search(serviceSearchConditionForm, Optional.empty(), PageRequest.of(0, 10));
        // then
        assertThat(response.getContent().size()).isEqualTo(2);
        assertThat(response.getContent().stream().map(PopularityServiceDto::getServiceName))
                .containsAnyOf("깃허브", "유튜브");
    }

    @Test
    @DisplayName("서비스 이름 , 카테고리 동시 검색")
    public void givenServiceNameAndCategories_whenLikeMatchSearchingService_thenReturnFitTheConditionServices() throws Exception {
        // given
        ServiceSearchConditionForm serviceSearchConditionForm =
                ServiceSearchConditionForm.of(List.of("BLOG"), "브");
        // when
        Page<PopularityServiceDto> response = serviceSearch.search(serviceSearchConditionForm, Optional.empty(), PageRequest.of(0, 10));
        // then
        assertThat(response.getContent().size()).isEqualTo(1);
        assertThat(response.getContent().stream().map(PopularityServiceDto::getServiceName))
                .containsAnyOf("깃허브");
    }

    @Test
    @DisplayName("전체 조회")
    public void givenNothing_whenSearchingService_thenReturnServices() throws Exception {
        // given
        ServiceSearchConditionForm serviceSearchConditionForm =
                ServiceSearchConditionForm.of(null, null);
        // when
        Page<PopularityServiceDto> response = serviceSearch.search(serviceSearchConditionForm, Optional.of(1L), PageRequest.of(0, 10));
        // then
        assertThat(response.getContent().size()).isEqualTo(4);
    }


    @Test
    @DisplayName("사용자 소유 여부")
    public void givenAuthenticationUser_whenSearchingServicePage_thenReturnServicesWithPossess() throws Exception {
        // given
        Services services = servicesSteps.create();
        Category category = categorySteps.create("DEFAULT");
        serviceCategorySteps.create(category, services);
        customerServiceSteps.create(1L, services);

        ServiceSearchConditionForm serviceSearchConditionForm =
                ServiceSearchConditionForm.of(null, null);
        // when
        Page<PopularityServiceDto> response = serviceSearch.search(serviceSearchConditionForm, Optional.of(1L), PageRequest.of(0, 10));
        // then
        assertThat(response.getContent().size()).isEqualTo(5);
        assertThat(response.stream().filter(PopularityServiceDto::isPossess).count())
                .isEqualTo(1);
    }

}