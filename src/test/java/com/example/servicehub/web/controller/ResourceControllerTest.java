package com.example.servicehub.web.controller;

import java.util.Map;
import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.example.servicehub.config.ProjectTestConfig;
import com.example.servicehub.support.file.FileManager;
import com.example.servicehub.support.file.LogoManager;
import com.example.servicehub.support.file.ProfileManager;
import com.example.servicehub.web.WithMockCustomUser;
import com.example.servicehub.web.controller.file.ResourceController;

@Import({ProjectTestConfig.class})
@DisplayName("ResourceController 테스트")
@WebMvcTest(ResourceController.class)
class ResourceControllerTest {

	@Autowired
	private MockMvc mockMVc;

	@MockBean
	private Map<String, FileManager> fileManagers;

	@Autowired
	private LogoManager logoManager;

	@Autowired
	private ProfileManager profileManager;

	@DisplayName("프로필 파일 테스트")
	@ParameterizedTest(name = "{0} 타입 {1} 파일")
	@MethodSource(value = "fileTest")
	@WithMockCustomUser
	void getProfileFile(final String fileType, final String filename) throws Exception {

		BDDMockito.given(fileManagers.get(fileType)).willReturn(getFileManager(fileType));

		mockMVc.perform(MockMvcRequestBuilders.get("/file/{fileType}/{filename}", fileType, filename))
			.andExpect(MockMvcResultMatchers.status().isOk());

	}

	private FileManager getFileManager(final String fileType) {
		switch (fileType) {
			case "logo":
				return logoManager;
			case "profile":
				return profileManager;
		}
		throw new IllegalStateException();
	}

	static Stream<Arguments> fileTest() {
		return Stream.of(
			Arguments.of("profile", "default.png"),
			Arguments.of("logo", "default.png")
		);
	}
}
