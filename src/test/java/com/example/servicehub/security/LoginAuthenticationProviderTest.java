package com.example.servicehub.security;

import com.example.servicehub.config.CustomerServer;
import com.example.servicehub.security.authentication.CustomerPrincipal;
import com.example.servicehub.security.authentication.LoginAuthenticationProvider;
import com.example.servicehub.util.JsonMaker;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@ExtendWith(MockitoExtension.class)
class LoginAuthenticationProviderTest {

    @Mock
    private CustomerServer customerServer;

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
    public void getCustomerInfoFromCustomerServerTest() throws Exception {

        String baseUrl = mockWebServer.url("/").toString();


        BDDMockito.given(customerServer.getServer()).willReturn(baseUrl);

        BDDMockito.given(customerServer.getClientId()).willReturn("clientId");


        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody(stubCustomerDto())
                .setHeader("Content-Type", MediaType.APPLICATION_JSON));

        LoginAuthenticationProvider loginAuthenticationProvider = new LoginAuthenticationProvider(customerServer, new RestTemplate());

        String token = "accessToken";
        Authentication beforeAuthenticate = new PreAuthenticatedAuthenticationToken(new CustomerPrincipal(), token);
        Authentication authenticate = loginAuthenticationProvider.authenticate(beforeAuthenticate);

        CustomerPrincipal customerPrincipal = (CustomerPrincipal) authenticate.getPrincipal();

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