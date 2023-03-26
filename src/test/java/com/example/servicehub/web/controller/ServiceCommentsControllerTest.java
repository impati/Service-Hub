package com.example.servicehub.web.controller;

import com.example.servicehub.config.TestSecurityConfig;
import com.example.servicehub.dto.ServiceCommentForm;
import com.example.servicehub.dto.ServiceCommentUpdateForm;
import com.example.servicehub.dto.SingleServiceWithCommentsDto;
import com.example.servicehub.service.ServiceCommentsAdminister;
import com.example.servicehub.service.SingleServiceSearch;
import com.example.servicehub.util.FormDataEncoder;
import com.example.servicehub.web.WithMockCustomUser;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Optional;

@DisplayName("ServiceCommentsController 테스트")
@Import(TestSecurityConfig.class)
@WebMvcTest(ServiceCommentsController.class)
class ServiceCommentsControllerTest {

    private final FormDataEncoder formDataEncoder = new FormDataEncoder();
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private ServiceCommentsAdminister serviceCommentsAdminister;
    @MockBean
    private SingleServiceSearch singleServiceSearch;

    @Test
    @DisplayName("서비스에 댓글 달기 테스트")
    @WithMockCustomUser(id = 2L)
    public void givenServicePage_whenWritingCommentAboutService_thenAddCommentOfServicePage() throws Exception {

        ServiceCommentForm serviceCommentForm = new ServiceCommentForm(1L, "hello", 2L, "impati");

        BDDMockito.willDoNothing().given(serviceCommentsAdminister).addServiceComment(serviceCommentForm);

        mockMvc.perform(
                        MockMvcRequestBuilders.post("/comments")
                                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                                .content(formDataEncoder.encode(serviceCommentForm, ServiceCommentForm.class))
                                .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.view().name("redirect:/service/" + serviceCommentForm.getServiceId()));

        BDDMockito.then(serviceCommentsAdminister).should().addServiceComment(serviceCommentForm);
    }

    @Test
    @DisplayName("서비스에 댓글 달기 테스트 - 2000길이 넘기는 오류")
    @WithMockCustomUser(id = 2L)
    public void givenServicePage_whenWritingCommentOver2000_thenError() throws Exception {

        ServiceCommentForm serviceCommentForm = new ServiceCommentForm(1L, greaterThan2000InLength(), 2L, "impati");

        BDDMockito.willDoNothing().given(serviceCommentsAdminister).addServiceComment(serviceCommentForm);

        mockMvc.perform(
                        MockMvcRequestBuilders.post("/comments")
                                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                                .content(formDataEncoder.encode(serviceCommentForm, ServiceCommentForm.class))
                                .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(MockMvcResultMatchers.model().attributeExists("contentError", "hasError"))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection());

        BDDMockito.then(serviceCommentsAdminister).shouldHaveNoInteractions();
    }


    private String greaterThan2000InLength() {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < 1001; i++) {
            stringBuilder.append("AB");
        }
        return stringBuilder.toString();
    }


    @Test
    @DisplayName("댓글 수정 페이지 테스트")
    @WithMockCustomUser(id = 2L)
    public void givenServicePage_whenClickCommentButton_thenRenderCommentEditPage() throws Exception {

        SingleServiceWithCommentsDto singleServiceWithCommentsDto = new SingleServiceWithCommentsDto();


        BDDMockito.given(singleServiceSearch.searchWithComments(1L, Optional.of(2L)))
                .willReturn(singleServiceWithCommentsDto);

        BDDMockito.given(serviceCommentsAdminister.bringCommentContent(22L))
                .willReturn("hello world");


        mockMvc.perform(
                        MockMvcRequestBuilders.get("/comments/edit")
                                .param("serviceId", "1")
                                .param("commentId", "22"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.model().attributeExists("singleServiceWithCommentsDto", "commentContent"))
                .andExpect(MockMvcResultMatchers.model().attribute("commentContent", "hello world"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("serviceCommentUpdateForm"))
                .andExpect(MockMvcResultMatchers.view().name("service/service-edit-page"));

    }


    @Test
    @DisplayName("댓글 수정 테스트")
    @WithMockCustomUser(id = 2L)
    public void givenServiceCommentEditPage_whenEditingComment_thenEditComment() throws Exception {

        ServiceCommentUpdateForm serviceCommentUpdateForm = new ServiceCommentUpdateForm(99L, 2L, "test", 2L);

        BDDMockito.willDoNothing().given(serviceCommentsAdminister).updateServiceComment(serviceCommentUpdateForm);

        mockMvc.perform(
                        MockMvcRequestBuilders.post("/comments/edit")
                                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                                .content(formDataEncoder.encode(serviceCommentUpdateForm, ServiceCommentUpdateForm.class))
                                .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(MockMvcResultMatchers.redirectedUrl("/service/" + serviceCommentUpdateForm.getServiceId()))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection());

        BDDMockito.then(serviceCommentsAdminister).should().updateServiceComment(serviceCommentUpdateForm);

    }

    @Test
    @DisplayName("댓글 삭제 테스트")
    @WithMockCustomUser(id = 3L)
    public void given_when_then() throws Exception {

        BDDMockito.willDoNothing().given(serviceCommentsAdminister).deleteServiceComment(99L, 3L);

        mockMvc.perform(
                        MockMvcRequestBuilders.delete("/comments/delete")
                                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                                .param("commentId", "99")
                                .param("serviceId", "3")
                                .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("OK"));

        BDDMockito.then(serviceCommentsAdminister).should().deleteServiceComment(99L, 3L);

    }

}