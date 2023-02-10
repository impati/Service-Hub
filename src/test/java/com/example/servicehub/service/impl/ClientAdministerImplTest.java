package com.example.servicehub.service.impl;

import com.example.servicehub.config.TestJpaConfig;
import com.example.servicehub.domain.Client;
import com.example.servicehub.dto.ClientEditForm;
import com.example.servicehub.repository.ClientRepository;
import com.example.servicehub.service.ClientAdminister;
import com.example.servicehub.support.ProfileManager;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import javax.persistence.EntityNotFoundException;


@DisplayName("클라이언트 정보 수정 및 조회 테스트")
@DataJpaTest
@Import({TestJpaConfig.class,ClientAdministerImpl.class, ProfileManager.class})
class ClientAdministerImplTest {

    @Autowired
    private ClientAdminister clientAdminister;
    @Autowired
    private ClientRepository clientRepository;

    @Test
    @DisplayName("클라이언트 닉네임으로 조회")
    public void givenNickname_whenFindingClient_thenReturnClient() throws Exception{
        // given
        Long adminId = 1L;
        // when
        Client admin = clientAdminister.findClientByClientId(adminId);
        // then
        Assertions.assertThat(admin.getEmail()).isEqualTo("sinms1116@naver.com");
    }

    @Test
    @DisplayName("클라이언트 닉네임으로 조회 - 조회가 되지 않는 경우")
    public void givenUnknownNickname_whenFindingClient_thenThrowEntityNotFound() throws Exception{
        // given
        Long adminId = -1L;
        // when
        // then
        Assertions.assertThatCode(()->clientAdminister.findClientByClientId(adminId))
                .isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    @DisplayName("클라이언트 정보 수정")
    public void givenClientEditForm_whenEditingClient_thenEditClient() throws Exception{
        // given
        String nickname = "성급한";
        ClientEditForm clientEditForm = new ClientEditForm(nickname, "hi hello world", "https://github.com/impati/Service-Hub");
        // when
        clientAdminister.editClientProfile(1L,clientEditForm);
        // then
        Client admin = clientRepository.findById(1L).get();

        Assertions.assertThat(admin.getNickname()).isEqualTo(nickname);
        Assertions.assertThat(admin.getIntroduceComment()).isEqualTo("hi hello world");
        Assertions.assertThat(admin.getBlogUrl()).isEqualTo("https://github.com/impati/Service-Hub");
        Assertions.assertThat(admin.getProfileImageUrl()).isEqualTo("http://impatient.iptime.org:9090/file/profile/default.png");
    }


}