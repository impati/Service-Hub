package com.example.servicehub.security.filter;

import com.example.servicehub.config.CustomerServer;
import com.example.servicehub.util.JsonMaker;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class LoginAuthenticationFilterTest {

    @Mock
    private CustomerServer customerServer;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

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
    @DisplayName("엑세스 토큰을 얻오는 테스트")
    public void getAccessTokenTest() throws Exception {
        // given
        String baseUrl = mockWebServer.url("/").toString();

        LoginAuthenticationFilter loginAuthenticationFilter = new LoginAuthenticationFilter(customerServer, new RestTemplate());

        given(request.getParameter("code")).willReturn("code");

        given(customerServer.getServer()).willReturn(baseUrl);

        given(customerServer.getClientId()).willReturn("clientId");


        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody(stubToken())
                .setHeader("Content-Type", MediaType.APPLICATION_JSON));

        // when
        Authentication authentication = loginAuthenticationFilter.attemptAuthentication(request, response);

        // then

        RecordedRequest recordedRequest = mockWebServer.takeRequest();
        assertThat(recordedRequest.getHeader("clientId")).isEqualTo("clientId");
        assertThat(recordedRequest.getHeader("Authorization")).isEqualTo("code");
        assertThat(authentication.getCredentials()).isNotNull();
        assertThat((String) authentication.getCredentials()).isEqualTo("token");

    }

    private String stubToken() {
        JsonMaker jsonMaker = new JsonMaker();
        return jsonMaker.make(Map.of("accessToken", "token"));
    }

}