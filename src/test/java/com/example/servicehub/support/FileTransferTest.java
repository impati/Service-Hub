package com.example.servicehub.support;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import com.example.servicehub.dto.common.MultipartFileForm;
import com.example.servicehub.dto.requestService.RequestServiceArticleForm;
import com.example.servicehub.support.file.FileTransfer;
import com.example.servicehub.support.file.LogoManager;

@ExtendWith(MockitoExtension.class)
class FileTransferTest {

	private FileTransfer<MultipartFileForm> fileTransfer;

	@Mock
	private LogoManager logoManager;

	@BeforeEach
	void setup() {
		fileTransfer = new FileTransfer<>(logoManager);
	}

	@Test
	@DisplayName("RequestServiceArticleForm 객체의 MultipartFile 저장하고 StoreName을 설정 테스트")
	void RequestServiceArticleFormFileTransferStoreNameTest() {
		// given
		final MultipartFile mockFile = mock(MultipartFile.class);
		final RequestServiceArticleForm fileForm = create(mockFile);
		given(logoManager.tryToRestore(mockFile)).willReturn("default.png");

		//when
		fileTransfer.transferLogoFileToStoreName(fileForm);

		//then
		assertThat(fileForm.getStoreName()).isEqualTo("default.png");
	}

	private RequestServiceArticleForm create(final MultipartFile multipartFile) {
		final RequestServiceArticleForm fileForm = new RequestServiceArticleForm();
		fileForm.setFile(multipartFile);
		return fileForm;
	}
}
