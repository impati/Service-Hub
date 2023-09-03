package com.example.servicehub.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.io.IOException;
import java.util.Collections;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.client.RestTemplate;

import com.example.servicehub.config.CustomerServer;
import com.example.servicehub.domain.customer.RoleType;
import com.example.servicehub.security.authentication.CustomerPrincipal;
import com.example.servicehub.service.customer.CustomerEditor;
import com.example.servicehub.support.file.ProfileManager;
import com.example.servicehub.web.dto.customer.CustomerEditForm;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;

@DisplayName("사용자 수정 테스트")
@ExtendWith(MockitoExtension.class)
class CustomerEditorTest {

	@InjectMocks
	private CustomerEditor customerEditor;

	@Mock
	private CustomerServer customerServer;

	@Mock
	private ProfileManager profileManager;

	@Mock
	private RestTemplate restTemplate;

	private MockWebServer mockWebServer;

	@BeforeEach
	void setup() throws IOException {
		mockWebServer = new MockWebServer();
		mockWebServer.start();
	}

	@AfterEach
	void cleanup() throws IOException {
		mockWebServer.shutdown();
	}

	@Test
	@DisplayName("customer-server 에 사용자 수정 테스트")
	void customerEditorTest() {
		createPrincipal();
		final String baseUrl = String.format("http://localhost:%s", mockWebServer.getPort());
		given(customerServer.getServer())
			.willReturn(baseUrl);
		given(profileManager.tryToRestore(null))
			.willReturn("default.png");

		mockWebServer.enqueue(new MockResponse().setResponseCode(200));

		customerEditor.edit(new CustomerEditForm("hi", "hello", "https://impati.github.io"));

		final CustomerPrincipal principal = (CustomerPrincipal)SecurityContextHolder.getContext()
			.getAuthentication()
			.getPrincipal();

		assertThat(principal.getId()).isEqualTo(1L);
		assertThat(principal.getNickname()).isEqualTo("hi");
		assertThat(principal.getBlogUrl()).isEqualTo("https://impati.github.io");
		assertThat(principal.getIntroduceComment()).isEqualTo("hello");
		assertThat(principal.getProfileImageUrl()).isEqualTo(getCustomerPrincipal().getProfileImageUrl());
	}

	private void createPrincipal() {
		final Authentication authentication = UsernamePasswordAuthenticationToken
			.authenticated(getCustomerPrincipal(), "Bearer AccessToken",
				Collections.singletonList(new SimpleGrantedAuthority("ROLE_ADMIN")));
		final SecurityContext context = SecurityContextHolder.getContext();
		context.setAuthentication(authentication);
	}

	private CustomerPrincipal getCustomerPrincipal() {
		return CustomerPrincipal.builder()
			.id(1L)
			.username("test")
			.profileImageUrl("test")
			.email("test")
			.blogUrl("test")
			.introduceComment("test")
			.roleType(RoleType.of("ROLE_ADMIN"))
			.authorities(Collections.singletonList(new SimpleGrantedAuthority("ROLE_ADMIN")))
			.build();
	}
}
