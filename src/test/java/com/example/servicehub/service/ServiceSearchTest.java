package com.example.servicehub.service;

import static org.assertj.core.api.Assertions.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import com.example.servicehub.config.StepsConfig;
import com.example.servicehub.config.TestJpaConfig;
import com.example.servicehub.domain.category.Category;
import com.example.servicehub.domain.services.Services;
import com.example.servicehub.dto.services.PopularityServiceDto;
import com.example.servicehub.dto.services.ServiceSearchConditionForm;
import com.example.servicehub.service.services.ServiceSearch;
import com.example.servicehub.service.services.ServiceSearchImpl;
import com.example.servicehub.steps.CategorySteps;
import com.example.servicehub.steps.CustomerServiceSteps;
import com.example.servicehub.steps.ServiceCategorySteps;
import com.example.servicehub.steps.ServicesSteps;

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
		final Category it = categorySteps.create("IT");
		final Category blog = categorySteps.create("BLOG");
		final Category job = categorySteps.create("JOB");

		final Services notion = servicesSteps.create("노션", "https://notion.so");
		final Services github = servicesSteps.create("깃허브", "https://github.com");
		final Services youtube = servicesSteps.create("유튜브", "https://youtube.com");
		final Services jobKorea = servicesSteps.create("잡코리아", "https://jobkorea.com");

		serviceCategorySteps.create(blog, notion);
		serviceCategorySteps.create(it, notion);
		serviceCategorySteps.create(it, github);
		serviceCategorySteps.create(blog, github);
		serviceCategorySteps.create(it, youtube);
		serviceCategorySteps.create(job, jobKorea);
	}

	@Test
	@DisplayName("카테고리로 검색 - 단일")
	void givenCategory_whenSearchingService_thenReturnFitTheConditionServices() {
		// given
		final ServiceSearchConditionForm serviceSearchConditionForm = ServiceSearchConditionForm.of(
			List.of("IT"),
			null
		);

		// when
		final Page<PopularityServiceDto> response = serviceSearch.search(
			serviceSearchConditionForm,
			Optional.empty(),
			PageRequest.of(0, 10)
		);

		// then
		assertThat(response.getContent())
			.hasSize(3)
			.extracting(PopularityServiceDto::getServiceName)
			.containsAnyOf("노션", "깃허브", "유튜브");
	}

	@Test
	@DisplayName("카테고리로 검색 - 멀티")
	void givenCategories_whenSearchingService_thenReturnFitTheConditionServices() {
		// given
		final ServiceSearchConditionForm serviceSearchConditionForm = ServiceSearchConditionForm.of(
			List.of("JOB", "BLOG"),
			null
		);

		// when
		final Page<PopularityServiceDto> response = serviceSearch.search(
			serviceSearchConditionForm,
			Optional.empty(),
			PageRequest.of(0, 10)
		);

		// then
		assertThat(response.getContent()).hasSize(3)
			.extracting(PopularityServiceDto::getServiceName)
			.containsAnyOf("잡코리아", "깃허브", "노션");
	}

	@Test
	@DisplayName("서비스 이름 검색 - 정확히 입력했을 때")
	void givenServiceName_whenExactMatchSearchingService_thenReturnFitTheConditionServices() {
		// given
		final ServiceSearchConditionForm serviceSearchConditionForm = ServiceSearchConditionForm.of(
			null,
			"깃허브"
		);

		// when
		final Page<PopularityServiceDto> response = serviceSearch.search(
			serviceSearchConditionForm,
			Optional.empty(),
			PageRequest.of(0, 10)
		);

		// then
		assertThat(response.getContent())
			.hasSize(1)
			.extracting(PopularityServiceDto::getServiceName)
			.contains("깃허브");
	}

	@Test
	@DisplayName("서비스 이름 검색 - 일부 입력했을 때")
	void givenServiceName_whenLikeMatchSearchingService_thenReturnFitTheConditionServices() {
		// given
		final ServiceSearchConditionForm serviceSearchConditionForm = ServiceSearchConditionForm.of(
			null,
			"브"
		);

		// when
		final Page<PopularityServiceDto> response = serviceSearch.search(
			serviceSearchConditionForm,
			Optional.empty(),
			PageRequest.of(0, 10)
		);

		// then
		assertThat(response.getContent())
			.hasSize(2)
			.extracting(PopularityServiceDto::getServiceName)
			.containsAnyOf("깃허브", "유튜브");
	}

	@Test
	@DisplayName("서비스 이름 , 카테고리 동시 검색")
	void givenServiceNameAndCategories_whenLikeMatchSearchingService_thenReturnFitTheConditionServices() {
		// given
		final ServiceSearchConditionForm serviceSearchConditionForm = ServiceSearchConditionForm.of(
			List.of("BLOG"),
			"브"
		);

		// when
		final Page<PopularityServiceDto> response = serviceSearch.search(
			serviceSearchConditionForm,
			Optional.empty(),
			PageRequest.of(0, 10)
		);

		// then
		assertThat(response.getContent())
			.hasSize(1)
			.extracting(PopularityServiceDto::getServiceName)
			.containsAnyOf("깃허브");
	}

	@Test
	@DisplayName("전체 조회")
	void givenNothing_whenSearchingService_thenReturnServices() {
		// given
		final ServiceSearchConditionForm serviceSearchConditionForm = ServiceSearchConditionForm.of(
			null,
			null
		);

		// when
		final Page<PopularityServiceDto> response = serviceSearch.search(
			serviceSearchConditionForm,
			Optional.of(1L),
			PageRequest.of(0, 10)
		);

		// then
		assertThat(response.getContent()).hasSize(4);
	}

	@Test
	@DisplayName("사용자 소유 여부")
	void givenAuthenticationUser_whenSearchingServicePage_thenReturnServicesWithPossess() {
		// given
		final Services services = servicesSteps.create();
		final Category category = categorySteps.create("DEFAULT");
		serviceCategorySteps.create(category, services);
		customerServiceSteps.create(1L, services);

		final ServiceSearchConditionForm serviceSearchConditionForm = ServiceSearchConditionForm.of(
			null,
			null
		);

		// when
		final Page<PopularityServiceDto> response = serviceSearch.search(
			serviceSearchConditionForm,
			Optional.of(1L),
			PageRequest.of(0, 10)
		);

		// then
		assertThat(response.getContent()).hasSize(5);
		assertThat(response.stream().filter(PopularityServiceDto::isPossess).count())
			.isEqualTo(1);
	}
}
