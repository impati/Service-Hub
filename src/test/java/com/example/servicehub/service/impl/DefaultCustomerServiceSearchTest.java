package com.example.servicehub.service.impl;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

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
		serviceCategorySteps.create(it, youtube);

		serviceCategorySteps.create(it, jobKorea);
		serviceCategorySteps.create(job, jobKorea);

		final Long customerId = 1L;
		customerServiceSteps.createWithClick(customerId, notion, 50);
		customerServiceSteps.createWithClick(customerId, github, 10);
		customerServiceSteps.createWithClick(customerId, youtube, 2);
	}

	@Test
	@DisplayName("클릭 수 정렬 테스트")
	void givenSearchCondition_whenSearchingFitTheConditionDefaultSortByClick_thenSearchedService() {
		// given
		final ServiceSearchConditionForm condition = ServiceSearchConditionForm.of(List.of("IT", "BLOG", "JOB"), null);
		// when
		final List<ClickServiceDto> response = customerServiceSearch.servicesOfCustomer(1L, condition);
		// then
		assertThat(response)
			.hasSize(3)
			.extracting(ClickServiceDto::getServiceName, ClickServiceDto::getClick)
			.contains(
				tuple("노션", 50L),
				tuple("깃허브", 10L),
				tuple("유튜브", 2L));
	}

	@Test
	@DisplayName("카테고리 테스트")
	void givenCategoryCondition_whenSearchingFitTheConditionDefaultSortByClick_thenSearchedService() {
		// given
		final ServiceSearchConditionForm condition = ServiceSearchConditionForm.of(List.of("BLOG"), null);

		// when
		final List<ClickServiceDto> response = customerServiceSearch.servicesOfCustomer(1L, condition);

		// then
		assertThat(response).hasSize(1)
			.extracting(ClickServiceDto::getServiceName)
			.contains("노션");
	}

	@Test
	@DisplayName("서비스 이름으로 검색")
	void searchServiceName() {
		// given
		ServiceSearchConditionForm condition = ServiceSearchConditionForm.of(null, "브");

		// when
		List<ClickServiceDto> response = customerServiceSearch.servicesOfCustomer(1L, condition);

		// then
		assertThat(response).hasSize(2)
			.extracting(ClickServiceDto::getServiceName, ClickServiceDto::getClick)
			.contains(
				tuple("깃허브", 10L),
				tuple("유튜브", 2L));
	}
}
