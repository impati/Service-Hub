package com.example.servicehub.service.customer;

import java.util.Objects;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.example.servicehub.config.CustomerServerConfig;
import com.example.servicehub.config.ServerConfig;
import com.example.servicehub.security.authentication.CustomerPrincipal;
import com.example.servicehub.support.file.ProfileManager;
import com.example.servicehub.web.dto.customer.CustomerEditForm;
import com.example.servicehub.web.dto.customer.CustomerEditRequest;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CustomerEditor {

	private static final String EDIT_URL = "/api/v1/customer";

	private final ServerConfig serverConfig;
	private final CustomerServerConfig customerServerConfig;
	private final ProfileManager profileManager;
	private final RestTemplate restTemplate;

	public void edit(final CustomerEditForm customerEditForm) {
		final CustomerEditRequest customerEditRequest = convertFrom(customerEditForm);
		final RequestEntity<CustomerEditRequest> request = RequestEntity.patch(
				customerServerConfig.getServer() + EDIT_URL)
			.contentType(MediaType.APPLICATION_JSON)
			.header(HttpHeaders.AUTHORIZATION, "Bearer " + getAccessToken())
			.body(customerEditRequest);
		restTemplate.exchange(request, Void.class);
		synchronization(customerEditRequest);
	}

	private CustomerEditRequest convertFrom(final CustomerEditForm customerEditForm) {
		final String storeName = profileManager.tryToRestore(customerEditForm.getProfileImage());
		return CustomerEditRequest.builder()
			.blogUrl(customerEditForm.getBlogUrl())
			.introduceComment(customerEditForm.getIntroComment())
			.nickname(customerEditForm.getNickname())
			.profileUrl(getProfileUrl(storeName))
			.build();
	}

	private String getProfileUrl(final String storeName) {
		if (Objects.equals(storeName, "default.png")) {
			return null;
		}

		return serverConfig.getDomain() + "/file/profile/" + storeName;
	}

	private String getAccessToken() {
		return (String)SecurityContextHolder.getContext().getAuthentication().getCredentials();
	}

	private void synchronization(final CustomerEditRequest customerEditRequest) {
		final CustomerPrincipal principal = (CustomerPrincipal)SecurityContextHolder.getContext()
			.getAuthentication()
			.getPrincipal();

		principal.update(
			customerEditRequest.getNickname(),
			customerEditRequest.getBlogUrl(),
			customerEditRequest.getIntroduceComment(),
			customerEditRequest.getProfileUrl());
	}
}
