package com.example.servicehub.web.controller;

import com.example.servicehub.config.TestSecurityConfig;
import com.example.servicehub.repository.ClientRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.TestExecutionEvent;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@DisplayName("Home 컨트롤러 테스트 - 로그인 , 회원가입 , 루트페이지 ")
@Import(TestSecurityConfig.class)
@WebMvcTest(HomeController.class)
class HomeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ClientRepository clientRepository;

    @Test
    @DisplayName("루트 페이지 테스트")
    @WithMockUser
    public void givenRootPathURL_whenEnterServiceHub_thenRedirectServiceSearchURL() throws Exception{

        mockMvc.perform(get("/"))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/service/search"))
                .andExpect(MockMvcResultMatchers.view().name("redirect:/service/search"));

    }

    @Test
    @DisplayName("회원 가입 페이지 테스트")
    @WithAnonymousUser
    public void givenSignupURL_whenClickSignupButton_thenRenderSignupPage() throws Exception{

        mockMvc.perform(get("/signup"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("signupForm"))
                .andExpect(MockMvcResultMatchers.view().name("client/signup"));

    }

    @Test
    @DisplayName("회원 가입 테스트 - 성공")
    @WithAnonymousUser(setupBefore = TestExecutionEvent.TEST_EXECUTION)
    public void givenSignupForm_whenValidAndSignup_thenSignupAndLogin() throws Exception{

        String testUsername = "test";
        String testEmail = "test@test.com";

        BDDMockito.given(clientRepository.existsClientByUsername(testUsername))
                .willReturn(false);

        BDDMockito.given(clientRepository.existsClientByEmail(testEmail))
                .willReturn(false);

        mockMvc.perform(
                post("/signup")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .content(createSignUpOnFORM_URLENCODED(testUsername,testEmail,"password123!@#","password123!@#"))
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                )
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.view().name("redirect:/keycloak/signup"));
                //.andExpect(MockMvcResultMatchers.redirectedUrl("/keycloak/signup"))// TODO: 회원 가입 방식을 변경 후 테스트 변경

        BDDMockito.then(clientRepository).should().existsClientByUsername(testUsername);
        BDDMockito.then(clientRepository).should().existsClientByEmail(testEmail);

    }

    @Test
    @DisplayName("회원 가입 테스트 실패 - 이미 존재하는 유저 네임과 이메일")
    public void givenSignupForm_whenValidFailCaseOfExistUsernameAndEmail_thenRenderSignupFormPageWithValidMessage() throws Exception{
        String testUsername = "test";
        String testEmail = "test@test.com";

        BDDMockito.given(clientRepository.existsClientByUsername(testUsername))
                .willReturn(true);

        BDDMockito.given(clientRepository.existsClientByEmail(testEmail))
                .willReturn(true);

        mockMvc.perform(
                        post("/signup")
                                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                                .content(createSignUpOnFORM_URLENCODED(testUsername,testEmail,"password123!@#","password123!@#"))
                                .with(SecurityMockMvcRequestPostProcessors.csrf())
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.model().hasErrors())
                .andExpect(MockMvcResultMatchers.model().errorCount(2))
                .andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("signupForm","username","email"))
                .andExpect(MockMvcResultMatchers.view().name("client/signup"));


        BDDMockito.then(clientRepository).should().existsClientByUsername(testUsername);
        BDDMockito.then(clientRepository).should().existsClientByEmail(testEmail);
    }

    @Test
    @DisplayName("회원 가입 테스트 실패 - 패스워드")
    public void givenSignupForm_whenValidFailCaseOfPassword_thenRenderSignupFormPageWithValidMessage() throws Exception{
        String testUsername = "test";
        String testEmail = "test@test.com";

        BDDMockito.given(clientRepository.existsClientByUsername(testUsername))
                .willReturn(false);

        BDDMockito.given(clientRepository.existsClientByEmail(testEmail))
                .willReturn(false);

        mockMvc.perform(
                        post("/signup")
                                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                                .content(createSignUpOnFORM_URLENCODED(testUsername,testEmail,"31!@#","31@#"))
                                .with(SecurityMockMvcRequestPostProcessors.csrf())
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.model().hasErrors())
                .andExpect(MockMvcResultMatchers.model().errorCount(2))
                .andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("signupForm","password","repeatPassword"))
                .andExpect(MockMvcResultMatchers.view().name("client/signup"));


        BDDMockito.then(clientRepository).should().existsClientByUsername(testUsername);
        BDDMockito.then(clientRepository).should().existsClientByEmail(testEmail);
    }

    private String createSignUpOnFORM_URLENCODED(String username , String email , String password,String repeatPassword){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("username=")
                .append(username).append("&")
                .append("email=")
                .append(email).append("&")
                .append("password=").append(password).append("&")
                .append("repeatPassword=")
                .append(repeatPassword);
        return stringBuilder.toString();
    }


    @Test
    @DisplayName("로그인 페이지 테스트")
    @WithMockUser
    public void givenLoginURL_whenClickLoginButton_thenRenderLoginPage() throws Exception{

        /**
         * model 에 null 값이 들어가는 경우 아예 담기지 않음
         */

        mockMvc.perform(get("/login"))
                .andExpect(MockMvcResultMatchers.view().name("client/signin"));


        mockMvc.perform(
                get("/login")
                        .queryParam("error","true")
                )
                .andExpect(MockMvcResultMatchers.model().attribute("error",true))
                .andExpect(MockMvcResultMatchers.view().name("client/signin"));

    }

}