package com.example.servicehub.security.filter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.web.client.RestTemplate;

import com.example.servicehub.config.CustomerServer;
import com.example.servicehub.security.authentication.CustomerPrincipal;

import lombok.Getter;

public class LoginAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

	private final static String DEFAULT_FILTER_PROCESSES_URI = "/login/customer-server/code/**";
	private final static String ACCESS_TOKEN_ENDPOINT = "/auth/gettoken";
	private final static String CLIENT_ID = "clientId";

	private final CustomerServer customerServer;
	private final RestTemplate restTemplate;

	public LoginAuthenticationFilter(final CustomerServer customerServer, final RestTemplate restTemplate) {
		super(DEFAULT_FILTER_PROCESSES_URI);
		this.customerServer = customerServer;
		this.restTemplate = restTemplate;
	}

	@Override
	public Authentication attemptAuthentication(
		final HttpServletRequest request,
		final HttpServletResponse response) throws AuthenticationException {
		final String code = request.getParameter("code");

		final ResponseEntity<AccessTokenDto> result = restTemplate.exchange(AccessTokenEndPointURL(), HttpMethod.POST,
			createRequestHeader(code), AccessTokenDto.class);

		final AccessTokenDto accessTokenDto = result.getBody();

		return new PreAuthenticatedAuthenticationToken(new CustomerPrincipal(), accessTokenDto.getAccessToken());
	}

	private String AccessTokenEndPointURL() {
		return customerServer.getServer() + ACCESS_TOKEN_ENDPOINT;
	}

	private HttpEntity createRequestHeader(String code) {
		final HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.add(CLIENT_ID, customerServer.getClientId());
		httpHeaders.add(HttpHeaders.AUTHORIZATION, code);
		return new HttpEntity<>(httpHeaders);
	}

	@Getter
	static class AccessTokenDto {
		private String accessToken;
	}
}
