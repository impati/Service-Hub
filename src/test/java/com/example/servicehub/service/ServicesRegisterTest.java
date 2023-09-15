package com.example.servicehub.service;

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

import com.example.servicehub.config.TestJpaConfig;
import com.example.servicehub.domain.services.Services;
import com.example.servicehub.dto.services.ServicesRegisterForm;
import com.example.servicehub.repository.services.ServiceCategoryRepository;
import com.example.servicehub.repository.services.ServicesRepository;
import com.example.servicehub.service.services.ServicesRegister;
import com.example.servicehub.support.file.DefaultImageResizer;
import com.example.servicehub.support.file.LogoManager;

@DisplayName("서비스 등록 테스트")
@DataJpaTest
@Import({
	TestJpaConfig.class,
	ServicesRegister.class,
	LogoManager.class,
	DefaultImageResizer.class
})
class ServicesRegisterTest {

	@Autowired
	private ServicesRegister servicesRegister;

	@Autowired
	private ServicesRepository servicesRepository;

	@Autowired
	private ServiceCategoryRepository serviceCategoryRepository;

	@Test
	@DisplayName("registerServices 메서드 오버로드 테스트")
	void givenArguments_whenRegisteringService_thenRegisterService() {
		// given
		final List<String> categoryList = List.of("IT");
		final String serviceUrl = "https://test.com/";
		final String title = "서비스 허브 입니다.";
		final String content = "hi 서비스 허브";
		final String name = "서비스 허브";
		final String logoStoreName = "default.png";

		// when
		servicesRegister.registerServices(
			categoryList,
			name,
			serviceUrl,
			title,
			content,
			logoStoreName
		);

		// then
		final Services services = servicesRepository.findByServiceUrl(serviceUrl).orElseThrow();
		assertThat(services.getServiceUrl()).isEqualTo(serviceUrl);
		serviceCategoryRepository.findByServices(services)
			.forEach(serviceCategory -> assertThat(serviceCategory.getCategory().getName()).contains("IT"));
	}
}
