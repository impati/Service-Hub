package com.example.servicehub.security;

import static org.mockito.BDDMockito.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.web.client.RestTemplate;

import com.example.servicehub.config.CustomerServerConfig;
import com.example.servicehub.security.authentication.CustomerPrincipal;
import com.example.servicehub.security.authentication.LoginAuthenticationProvider;
import com.example.servicehub.util.JsonMaker;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;

@ExtendWith(MockitoExtension.class)
class LoginAuthenticationProviderTest {

	@Mock
	private CustomerServerConfig customerServerConfig;

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
	@DisplayName("사용자 정보 얻어오기")
	void getCustomerInfoFromCustomerServerTest() {

		final String baseUrl = mockWebServer.url("/").toString();

		given(customerServerConfig.getServer()).willReturn(baseUrl);

		given(customerServerConfig.getClientId()).willReturn("clientId");

		mockWebServer.enqueue(new MockResponse()
			.setResponseCode(200)
			.setBody(stubCustomerDto())
			.setHeader("Content-Type", MediaType.APPLICATION_JSON));

		final LoginAuthenticationProvider loginAuthenticationProvider = new LoginAuthenticationProvider(
			customerServerConfig,
			new RestTemplate());

		final String token = "accessToken";
		final Authentication beforeAuthenticate = new PreAuthenticatedAuthenticationToken(
			new CustomerPrincipal(),
			token
		);
		final Authentication authenticate = loginAuthenticationProvider.authenticate(beforeAuthenticate);
		final CustomerPrincipal customerPrincipal = (CustomerPrincipal)authenticate.getPrincipal();

		Assertions.assertThat(customerPrincipal.getEmail()).isEqualTo("test@test.com");
		Assertions.assertThat(authenticate.getCredentials()).isEqualTo(token);
	}

	private String stubCustomerDto() {
		return new JsonMaker().make(getAttributes());
	}

	private Map<String, String> getAttributes() {
		Map<String, String> attr = new HashMap<>();
		attr.put("id", "99");
		attr.put("username", "test");
		attr.put("nickname", "test");
		attr.put("email", "test@test.com");
		attr.put("providerType", "KEYCLOAK");
		attr.put("roleType", "USER");
		attr.put("blogUrl", "https://impati.github.io");
		attr.put("profileImageUrl", "https://service-hub.org");
		attr.put("introduceComment", "안녕하세요");
		return attr;
	}
}
