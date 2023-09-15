package com.example.servicehub.security.authentication;

import java.util.Collections;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.web.client.RestTemplate;

import com.example.servicehub.config.CustomerServerConfig;
import com.example.servicehub.domain.customer.ProviderType;
import com.example.servicehub.domain.customer.RoleType;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class LoginAuthenticationProvider implements AuthenticationProvider {

	private static final String CUSTOMER_ENDPOINT = "/api/v1/customer";
	private static final String CLIENT_ID = "clientId";
	private final CustomerServerConfig customerServerConfig;
	private final RestTemplate restTemplate;

	@Override
	public Authentication authenticate(final Authentication authentication) throws AuthenticationException {
		final String token = getToken(authentication);

		final CustomerDto customer = getCustomer(token);

		final CustomerPrincipal customerPrincipal = customer.toPrincipal();

		return new PreAuthenticatedAuthenticationToken(customerPrincipal, token, customerPrincipal.getAuthorities());
	}

	private CustomerDto getCustomer(final String token) {
		return restTemplate.exchange(
			customerServerConfig.getServer() + CUSTOMER_ENDPOINT,
			HttpMethod.POST,
			createRequestHeader(token), CustomerDto.class).getBody();
	}

	@Override
	public boolean supports(final Class<?> authentication) {
		return PreAuthenticatedAuthenticationToken.class.isAssignableFrom(authentication);
	}

	private String getToken(final Authentication authentication) {
		return String.valueOf(authentication.getCredentials());
	}

	private HttpEntity createRequestHeader(final String accessToken) {
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.set(CLIENT_ID, customerServerConfig.getClientId());
		httpHeaders.add(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken);
		return new HttpEntity<>(httpHeaders);
	}

	@Getter
	@AllArgsConstructor
	@NoArgsConstructor
	static class CustomerDto {
		private Long id;
		private String username;
		private String nickname;
		private String email;
		private ProviderType providerType;
		private RoleType roleType;
		private String blogUrl;
		private String profileImageUrl;
		private String introduceComment;

		CustomerPrincipal toPrincipal() {
			return CustomerPrincipal.builder()
				.id(id)
				.username(username)
				.nickname(nickname)
				.email(email)
				.providerType(providerType)
				.roleType(roleType)
				.blogUrl(blogUrl)
				.profileImageUrl(profileImageUrl)
				.introduceComment(introduceComment)
				.authorities(Collections.singletonList(new SimpleGrantedAuthority(roleType.getName())))
				.build();
		}
	}
}
