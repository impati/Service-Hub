package com.example.servicehub.web.controller;

import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;

import com.example.servicehub.config.TestSecurityConfig;
import com.example.servicehub.domain.requestservice.RequestServiceArticle;
import com.example.servicehub.domain.requestservice.RequestStatus;
import com.example.servicehub.dto.requestService.RequestServiceArticleForm;
import com.example.servicehub.dto.requestService.RequestServiceArticleSearchCondition;
import com.example.servicehub.service.category.CategoryAdminister;
import com.example.servicehub.service.requestService.RequestServiceArticleRegister;
import com.example.servicehub.service.requestService.RequestServiceArticleSearch;
import com.example.servicehub.service.requestService.RequestServiceToServiceTransfer;
import com.example.servicehub.util.FormDataEncoder;
import com.example.servicehub.web.WithMockCustomUser;
import com.example.servicehub.web.controller.requestService.RequestServiceArticleController;

@WebMvcTest(RequestServiceArticleController.class)
@Import(TestSecurityConfig.class)
class RequestServiceArticleControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private RequestServiceArticleSearch requestServiceArticleSearch;

	@MockBean
	private RequestServiceToServiceTransfer requestServiceToServiceTransfer;

	@MockBean
	private CategoryAdminister categoryAdminister;

	@MockBean
	private RequestServiceArticleRegister requestServiceArticleRegister;

	private FormDataEncoder dataEncoder;

	@BeforeEach
	void setup() {
		dataEncoder = new FormDataEncoder();
	}

	@Test
	@DisplayName("등록 요청 서비스들 조회 테스트")
	void givenURL_whenSearchingRequestedService_thenRenderRequestServiceArticles() throws Exception {
		final RequestServiceArticleSearchCondition condition = new RequestServiceArticleSearchCondition(any(), any());
		final PageRequest pageRequest = PageRequest.of(0, 10);

		given(requestServiceArticleSearch.searchArticle(condition, pageRequest))
			.willReturn(new PageImpl<>(stubbing(), pageRequest, 1));

		mockMvc.perform(get("/requested-service")
				.content(dataEncoder.encode(condition, RequestServiceArticleSearchCondition.class)))
			.andExpect(status().isOk())
			.andExpect(view().name("requested-service/articles"))
			.andExpect(model().attributeExists("articles"))
			.andDo(print());
	}

	@Test
	@DisplayName("등록 요청 서비스 조회 테스트")
	void givenURL_whenSearchingRequestedService_thenRenderRequestServiceArticle() throws Exception {
		given(requestServiceArticleSearch.searchSingleArticle(1L))
			.willReturn(stubbingOne());

		given(categoryAdminister.getAllCategories())
			.willReturn(List.of("IT", "AI"));

		mockMvc.perform(get("/requested-service/{articleId}", 1L))
			.andExpect(status().isOk())
			.andExpect(view().name("requested-service/article"))
			.andExpect(model().attributeExists("article", "categoryNames"))
			.andDo(print());
	}

	@Test
	@DisplayName("등록 요청 서비스를 실제 서비스로 등록")
	void givenArticleId_whenTransferToServices_thenRegisterServices() throws Exception {
		final List<String> categories = List.of("IT", "AI");
		willDoNothing().given(requestServiceToServiceTransfer).registerRequestedServiceAsService(
			1L,
			categories,
			RequestStatus.COMPLETE
		);

		mockMvc.perform(post("/requested-service/{articleId}", 1L)
				.queryParam("categoryNames", "IT", "AI")
				.queryParam("status", "COMPLETE")
				.with(SecurityMockMvcRequestPostProcessors.csrf()))
			.andExpect(status().is3xxRedirection())
			.andExpect(handler().methodName("saveService"))
			.andExpect(view().name("redirect:/requested-service/" + 1L))
			.andDo(print());
	}

	@Test
	@DisplayName("등록 요청 서비스 등록 페이지 테스트")
	@WithMockCustomUser
	void givenURL_whenToRegisterRequestedService_thenRenderRequestServiceRegistrationPage() throws Exception {
		mockMvc.perform(get("/requested-service/registration"))
			.andExpect(status().isOk())
			.andExpect(view().name("requested-service/article-registration"))
			.andExpect(model().attributeExists("requestServiceArticleForm"))
			.andDo(print());
	}

	@Test
	@DisplayName("등록 요청 서비스 등록 테스트")
	@WithMockCustomUser
	void givenRequestServiceArticleForm_whenThenRegisterRequestServiceArticle() throws Exception {
		final RequestServiceArticleForm stub = new RequestServiceArticleForm();
		given(requestServiceArticleRegister.register(any(), any(), any())).willReturn(1L);

		mockMvc.perform(post("/requested-service/registration")
				.content(dataEncoder.encode(stub, RequestServiceArticleForm.class))
				.with(SecurityMockMvcRequestPostProcessors.csrf()))
			.andExpect(status().is3xxRedirection())
			.andExpect(handler().methodName("register"))
			.andExpect(view().name("redirect:/requested-service/" + 1L))
			.andDo(print());
	}

	private List<RequestServiceArticle> stubbing() {
		return List.of(RequestServiceArticle
			.builder()
			.serviceContent("test")
			.build());
	}

	private RequestServiceArticle stubbingOne() {
		return RequestServiceArticle
			.builder()
			.build();
	}
}
