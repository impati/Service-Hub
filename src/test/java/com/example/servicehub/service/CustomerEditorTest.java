package com.example.servicehub.service;


import com.example.servicehub.config.CustomerServer;
import com.example.servicehub.domain.RoleType;
import com.example.servicehub.security.authentication.CustomerPrincipal;
import com.example.servicehub.support.ProfileManager;
import com.example.servicehub.web.dto.CustomerEditForm;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.Collections;

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
    public void customerEditorTest() throws Exception {

        createPrincipal();

        String baseUrl = String.format("http://localhost:%s", mockWebServer.getPort());

        BDDMockito.given(customerServer.getServer())
                .willReturn(baseUrl);

        BDDMockito.given(profileManager.tryToRestore(null))
                .willReturn("default.png");

        mockWebServer.enqueue(new MockResponse().setResponseCode(200));

        customerEditor.edit(new CustomerEditForm("hi", "hello", "https://impati.github.io"));

        CustomerPrincipal principal = (CustomerPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Assertions.assertThat(principal.getId()).isEqualTo(1L);
        Assertions.assertThat(principal.getNickname()).isEqualTo("hi");
        Assertions.assertThat(principal.getBlogUrl()).isEqualTo("https://impati.github.io");
        Assertions.assertThat(principal.getIntroduceComment()).isEqualTo("hello");
    }

    private void createPrincipal() {
        Authentication authentication = UsernamePasswordAuthenticationToken
                .authenticated(getCustomerPrincipal(), "Bearer AccessToken", Collections.singletonList(new SimpleGrantedAuthority("ROLE_ADMIN")));
        SecurityContext context = SecurityContextHolder.getContext();
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