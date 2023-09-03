package com.example.servicehub.service;

import static java.util.stream.Collectors.*;
import static org.assertj.core.api.Assertions.*;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.mock.web.MockMultipartFile;

import com.example.servicehub.config.StepsConfig;
import com.example.servicehub.config.TestJpaConfig;
import com.example.servicehub.domain.category.Category;
import com.example.servicehub.domain.services.ServiceCategory;
import com.example.servicehub.domain.services.Services;
import com.example.servicehub.dto.services.ServiceUpdateForm;
import com.example.servicehub.repository.services.ServicesRepository;
import com.example.servicehub.service.services.ServiceUpdate;
import com.example.servicehub.steps.CategorySteps;
import com.example.servicehub.steps.ServiceCategorySteps;
import com.example.servicehub.steps.ServicesSteps;
import com.example.servicehub.support.file.DefaultImageResizer;
import com.example.servicehub.support.file.LogoManager;

@DisplayName("서비스 수정 테스트")
@DataJpaTest
@Import({
	TestJpaConfig.class,
	StepsConfig.class,
	ServiceUpdate.class,
	LogoManager.class,
	DefaultImageResizer.class
})
class ServiceUpdateTest {

	@Autowired
	private ServicesRepository servicesRepository;

	@Autowired
	private ServiceUpdate serviceUpdate;

	@Autowired
	private ServicesSteps servicesSteps;

	@Autowired
	private CategorySteps categorySteps;

	@Autowired
	private ServiceCategorySteps serviceCategorySteps;

	@Test
	@DisplayName("서비스 정보 업데이트 - 서비스 이름 업데이트")
	void givenServiceUpdateForm_whenUpdatingServiceName_thenUpdateService() {
		// given
		final Services services = servicesSteps.create();
		final ServiceUpdateForm updateForm = ServiceUpdateForm.from(services);

		// when
		final String updateServiceName = "update";
		updateForm.setServiceName(updateServiceName);
		serviceUpdate.update(updateForm);

		// then
		final Services updatedServices = servicesRepository.findById(services.getId()).get();
		assertThat(updatedServices.getServiceName())
			.isEqualTo(updateServiceName);
	}

	@Test
	@DisplayName("서비스 정보 업데이트 - 카테고리 업데이트 추가 ")
	void givenServiceUpdateForm_whenAddingAndUpdatingServiceCategories_thenUpdateService() {
		// given
		final Category category = categorySteps.create("IT");
		final Services services = servicesSteps.create();
		final ServiceUpdateForm updateForm = ServiceUpdateForm.from(services);

		// when
		final List<String> serviceCategoryNames = convertToCategoryNamesFrom(services.getServiceCategories());
		serviceCategoryNames.add(category.getName());
		updateForm.setCategoryNames(serviceCategoryNames);
		serviceUpdate.update(updateForm);

		// then
		final Services updatedServices = servicesRepository.findById(services.getId()).get();
		List<String> updatedServiceCategoryNames = convertToCategoryNamesFrom(updatedServices.getServiceCategories());
		assertThat(updatedServiceCategoryNames).containsAll(serviceCategoryNames);
	}

	@Test
	@DisplayName("서비스 정보 업데이트 - 카테고리 업데이트 삭제")
	void givenServiceUpdateForm_whenDeletingAndUpdatingServiceCategories_thenUpdateService() {
		// given
		final Category it = categorySteps.create("IT");
		final Category enter = categorySteps.create("ENTER");
		final Services services = servicesSteps.create();
		serviceCategorySteps.create(it, services);
		serviceCategorySteps.create(enter, services);
		final ServiceUpdateForm updateForm = ServiceUpdateForm.from(services);

		// when
		final List<String> serviceCategoryNames = convertToCategoryNamesFrom(services.getServiceCategories());
		serviceCategoryNames.remove(it.getName());
		updateForm.setCategoryNames(serviceCategoryNames);
		serviceUpdate.update(updateForm);

		// then
		final Services updatedServices = servicesRepository.findById(services.getId()).get();
		final List<String> updatedServiceCategoryNames = convertToCategoryNamesFrom(
			updatedServices.getServiceCategories()
		);
		assertThat(updatedServiceCategoryNames).containsAll(serviceCategoryNames);
		assertThat(updatedServiceCategoryNames).hasSize(3);
	}

	private List<String> convertToCategoryNamesFrom(final List<ServiceCategory> serviceCategories) {
		return serviceCategories
			.stream()
			.map(ServiceCategory::getCategory)
			.map(Category::getName).collect(toList());
	}

	@Test
	@DisplayName("서비스 정보 업데이트 - 로고 업데이트")
	void givenServiceUpdateForm_whenUpdatingLogoFile_thenUpdateService() throws IOException {
		// given
		final Services services = servicesSteps.create();
		final ServiceUpdateForm updateForm = ServiceUpdateForm.from(services);
		final String originStoreName = services.getLogoStoreName();

		// when
		MockMultipartFile mockMultipartFile = new MockMultipartFile(
			"test",
			"test.png",
			"image/png",
			new FileInputStream(
				"/Users/jun-yeongchoe/Desktop/project/ServiceHub/src/main/resources/image/default.png")
		);
		updateForm.setLogoFile(mockMultipartFile);
		serviceUpdate.update(updateForm);

		// then
		final Services updatedServices = servicesRepository.findById(services.getId()).get();
		assertThat(updatedServices.getLogoStoreName()).isNotNull();
		assertThat(updatedServices.getLogoStoreName()).isNotEqualTo(originStoreName);
	}

	@Test
	@DisplayName("서비스 정보 업데이트 - 로고 업데이트 하지 않기")
	void givenServiceFormWithNoLogoFile_whenNonUpdatingLogoFile_thenNothing() {
		// given
		final Services services = servicesSteps.create();
		final ServiceUpdateForm updateForm = ServiceUpdateForm.from(services);
		final String originStoreName = services.getLogoStoreName();

		// when
		serviceUpdate.update(updateForm);

		// then
		final Services updatedServices = servicesRepository.findById(services.getId()).get();
		assertThat(updatedServices.getLogoStoreName()).isNotNull();
		assertThat(updatedServices.getLogoStoreName()).isEqualTo(originStoreName);
	}
}
