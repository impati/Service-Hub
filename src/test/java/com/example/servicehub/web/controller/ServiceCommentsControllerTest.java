package com.example.servicehub.web.controller;

import static org.mockito.BDDMockito.*;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.example.servicehub.config.TestSecurityConfig;
import com.example.servicehub.dto.services.ServiceCommentForm;
import com.example.servicehub.dto.services.ServiceCommentUpdateForm;
import com.example.servicehub.dto.services.SingleServiceWithCommentsDto;
import com.example.servicehub.service.services.ServiceCommentsAdminister;
import com.example.servicehub.service.services.SingleServiceSearch;
import com.example.servicehub.util.FormDataEncoder;
import com.example.servicehub.web.WithMockCustomUser;
import com.example.servicehub.web.controller.services.ServiceCommentsController;

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
	void givenServicePage_whenWritingCommentAboutService_thenAddCommentOfServicePage() throws Exception {
		final ServiceCommentForm serviceCommentForm = new ServiceCommentForm(
			1L,
			"hello",
			2L,
			"impati"
		);

		willDoNothing().given(serviceCommentsAdminister).addServiceComment(serviceCommentForm);

		mockMvc.perform(
				MockMvcRequestBuilders.post("/comments")
					.contentType(MediaType.APPLICATION_FORM_URLENCODED)
					.content(formDataEncoder.encode(serviceCommentForm, ServiceCommentForm.class))
					.with(SecurityMockMvcRequestPostProcessors.csrf()))
			.andExpect(MockMvcResultMatchers.status().is3xxRedirection())
			.andExpect(MockMvcResultMatchers.view().name("redirect:/service/" + serviceCommentForm.getServiceId()));
	}

	@Test
	@DisplayName("서비스에 댓글 달기 테스트 - 2000길이 넘기는 오류")
	@WithMockCustomUser(id = 2L)
	void givenServicePage_whenWritingCommentOver2000_thenError() throws Exception {
		final ServiceCommentForm serviceCommentForm = new ServiceCommentForm(
			1L,
			greaterThan2000InLength(),
			2L,
			"impati"
		);

		willDoNothing().given(serviceCommentsAdminister).addServiceComment(serviceCommentForm);

		mockMvc.perform(
				MockMvcRequestBuilders.post("/comments")
					.contentType(MediaType.APPLICATION_FORM_URLENCODED)
					.content(formDataEncoder.encode(serviceCommentForm, ServiceCommentForm.class))
					.with(SecurityMockMvcRequestPostProcessors.csrf()))
			.andExpect(MockMvcResultMatchers.model().attributeExists("contentError", "hasError"))
			.andExpect(MockMvcResultMatchers.status().is3xxRedirection());
	}

	private String greaterThan2000InLength() {
		final StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("AB".repeat(1001));
		return stringBuilder.toString();
	}

	@Test
	@DisplayName("댓글 수정 페이지 테스트")
	@WithMockCustomUser(id = 2L)
	void givenServicePage_whenClickCommentButton_thenRenderCommentEditPage() throws Exception {
		final SingleServiceWithCommentsDto singleServiceWithCommentsDto = new SingleServiceWithCommentsDto();

		given(singleServiceSearch.searchWithComments(1L, Optional.of(2L))).willReturn(singleServiceWithCommentsDto);
		given(serviceCommentsAdminister.bringCommentContent(22L)).willReturn("hello world");

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
	void givenServiceCommentEditPage_whenEditingComment_thenEditComment() throws Exception {
		final ServiceCommentUpdateForm serviceCommentUpdateForm = new ServiceCommentUpdateForm(
			"test",
			99L,
			2L,
			2L
		);

		willDoNothing().given(serviceCommentsAdminister).updateServiceComment(serviceCommentUpdateForm);

		mockMvc.perform(
				MockMvcRequestBuilders.post("/comments/edit")
					.contentType(MediaType.APPLICATION_FORM_URLENCODED)
					.content(formDataEncoder.encode(serviceCommentUpdateForm, ServiceCommentUpdateForm.class))
					.with(SecurityMockMvcRequestPostProcessors.csrf()))
			.andExpect(MockMvcResultMatchers.redirectedUrl("/service/" + serviceCommentUpdateForm.getServiceId()))
			.andExpect(MockMvcResultMatchers.status().is3xxRedirection());
	}

	@Test
	@DisplayName("댓글 삭제 테스트")
	@WithMockCustomUser(id = 3L)
	void given_when_then() throws Exception {
		willDoNothing().given(serviceCommentsAdminister).deleteServiceComment(99L, 3L);

		mockMvc.perform(
				MockMvcRequestBuilders.delete("/comments/delete")
					.contentType(MediaType.APPLICATION_FORM_URLENCODED)
					.param("commentId", "99")
					.param("serviceId", "3")
					.with(SecurityMockMvcRequestPostProcessors.csrf()))
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.content().string("OK"));
	}
}
