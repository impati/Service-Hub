package com.example.servicehub.support;

import com.example.servicehub.dto.common.MultipartFileForm;
import com.example.servicehub.dto.requestService.RequestServiceArticleForm;
import com.example.servicehub.support.file.FileTransfer;
import com.example.servicehub.support.file.LogoManager;
import com.example.servicehub.support.file.ProfileManager;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class FileTransferTest {

    FileTransfer<MultipartFileForm> fileTransfer;

    @Mock
    private LogoManager logoManager;

    @Mock
    private ProfileManager profileManager;

    @BeforeEach
    void setup() {
        fileTransfer = new FileTransfer<>(logoManager, profileManager);
    }

    @Test
    @DisplayName("RequestServiceArticleForm 객체의 MultipartFile 저장하고 StoreName을 설정 테스트")
    public void RequestServiceArticleFormFileTransferStoreNameTest() throws Exception {

        // given
        MultipartFile mockFile = mock(MultipartFile.class);

        RequestServiceArticleForm fileForm = create(mockFile);

        given(logoManager.tryToRestore(mockFile)).willReturn("default.png");

        //when
        fileTransfer.transferLogoFileToStoreName(fileForm);

        //then
        Assertions.assertThat(fileForm.getStoreName()).isEqualTo("default.png");

    }

    private RequestServiceArticleForm create(MultipartFile multipartFile) {
        RequestServiceArticleForm fileForm = new RequestServiceArticleForm();
        fileForm.setFile(multipartFile);
        return fileForm;
    }
}