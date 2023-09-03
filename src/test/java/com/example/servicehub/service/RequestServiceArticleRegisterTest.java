package com.example.servicehub.service;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import com.example.servicehub.config.TestJpaConfig;
import com.example.servicehub.domain.requestservice.RequestServiceArticle;
import com.example.servicehub.domain.requestservice.RequestStatus;
import com.example.servicehub.dto.requestService.RequestServiceArticleForm;
import com.example.servicehub.repository.requestService.RequestServiceArticleRepository;
import com.example.servicehub.service.requestService.RequestServiceArticleRegister;

@DataJpaTest
@Import({TestJpaConfig.class, RequestServiceArticleRegister.class})
class RequestServiceArticleRegisterTest {

	@Autowired
	private RequestServiceArticleRepository repository;

	@Autowired
	private RequestServiceArticleRegister register;

	@Test
	@DisplayName("서비스 요청 게시글 등록 테스트")
	void givenRequestServiceInfo_whenRegisteringRequestService_thenRegisterRequestService() {
		// given
		final RequestServiceArticleForm form = create();

		// when
		register.register(1L, "impati", form);

		// then
		assertThat(repository.count()).isEqualTo(1);
	}

	@Test
	@DisplayName("서비스 요청 게시글 등록 필드 테스트")
	void givenRequestServiceInfo_whenRegisteringRequestService_thenAssertFieldAfterRegister() {
		// given
		final RequestServiceArticleForm form = create();

		// when
		register.register(1L, "impati", form);

		// then
		final RequestServiceArticle response = repository
			.findAll()
			.stream()
			.findFirst()
			.get();

		assertThat(response.getId()).isNotNull();
		assertThat(response.getArticleDescription()).isEqualTo("서비스 요청 드립니다.");
		assertThat(response.getArticleTitle()).isEqualTo("서비스 요청");
		assertThat(response.getServiceUrl()).isEqualTo("https://service-hub.org");
		assertThat(response.getServiceTitle()).isEqualTo("서비스 허브입니다.");
		assertThat(response.getServiceName()).isEqualTo("서비스 허브");
		assertThat(response.getServiceContent()).isEqualTo("서비스 허브를 소개합니다");
		assertThat(response.getLogoStoreName()).isEqualTo("default.png");
		assertThat(response.getRequestStatus()).isEqualTo(RequestStatus.BEFORE); // 서버에 저장된 서비스인지
	}

	private RequestServiceArticleForm create() {
		final RequestServiceArticleForm form = new RequestServiceArticleForm();
		form.setArticleDescription("서비스 요청 드립니다.");
		form.setArticleTitle("서비스 요청");
		form.setServiceUrl("https://service-hub.org");
		form.setServiceTitle("서비스 허브입니다.");
		form.setServiceName("서비스 허브");
		form.setServiceContent("서비스 허브를 소개합니다");
		form.setStoreName("default.png");
		return form;
	}
}
