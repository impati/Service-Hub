package com.example.servicehub.service.impl;

import com.example.servicehub.config.TestJpaConfig;
import com.example.servicehub.domain.Client;
import com.example.servicehub.domain.ClientService;
import com.example.servicehub.domain.ProviderType;
import com.example.servicehub.domain.Services;
import com.example.servicehub.dto.ClickServiceDto;
import com.example.servicehub.dto.ServiceSearchConditionForm;
import com.example.servicehub.repository.ClientRepository;
import com.example.servicehub.repository.ClientServiceRepository;
import com.example.servicehub.repository.ServicesRepository;
import com.example.servicehub.service.ClientServiceAdminister;
import com.example.servicehub.util.ProjectUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@DisplayName(" 사용자 서비스 기능 ")
@DataJpaTest
@Import({TestJpaConfig.class , ClientServiceAdministerImpl.class})
class dClientServiceAdministerImplTest {

    @Autowired private ClientServiceAdminister clientServiceAdminister;
    @Autowired private ClientRepository clientRepository;
    @Autowired private ClientServiceRepository clientServiceRepository;
    @Autowired private ServicesRepository servicesRepository;

    @Test
    @DisplayName("사용자 서비스 추가")
    public void givenClientIdAndServiceId_whenAdding_thenAddServiceOfClient() throws Exception{
        // given
        Client newClient = Client.of("test","test",
                "123","yongs@naver.com", "ROLE_USER", ProviderType.KEYCLOAK, ProjectUtils.getDomain() + "client",
                ProjectUtils.getDomain() +"/profile/default.png");
        clientRepository.save(newClient);

        // when
        clientServiceAdminister.addClientService(newClient.getId(), 1L);
        clientServiceAdminister.addClientService(newClient.getId(), 2L);
        clientServiceAdminister.addClientService(newClient.getId(), 3L);
        // then
        List<Services> serviceOfClient = clientServiceRepository
                .findServiceByClientId(newClient.getId());

        assertThat(serviceOfClient.size()).isEqualTo(3);
    }

    @Test
    @DisplayName("사용자 서비스 추가 중복시 무시")
    public void givenClientAndServiceDuplication_whenOneAdding_thenOneAddServiceForClient() throws Exception{
        // given
        Client newClient = Client.of("test","test",
                "123","yongs@naver.com", "ROLE_USER", ProviderType.KEYCLOAK,ProjectUtils.getDomain() + "client",
                ProjectUtils.getDomain() +"/profile/default.png");
        clientRepository.save(newClient);
        // when
        clientServiceAdminister.addClientService(newClient.getId(), 1L);
        clientServiceAdminister.addClientService(newClient.getId(), 1L);
        // then
        assertThat(clientServiceRepository.findServiceByClientId(newClient.getId()).size())
                .isEqualTo(1);
    }

    @Test
    @DisplayName(" 사용자 서비스 삭제 ")
    public void givenClientAndService_whenDeletingService_thenDelete() throws Exception{
        // given
        clientServiceAdminister.addClientService(1L, 1L);
        clientServiceAdminister.addClientService(1L, 2L);
        clientServiceAdminister.addClientService(1L, 3L);
        // when
        clientServiceAdminister.deleteClientService(1L,1L);
        clientServiceAdminister.deleteClientService(1L,1L);
        // then
        assertThat(clientServiceRepository.findServiceByClientId(1L).size())
                .isEqualTo(2);
    }

    @Test
    @DisplayName("사용자가 서비스를 이용한 횟수 테스트")
    public void givenClientService_whenClicking_thenReturnAddedClick() throws Exception{
        // given
        Client client = clientRepository.findById(1L).get();
        Services services = servicesRepository.findById(1L).get();
        clientServiceAdminister.countClickAndReturnUrl(1L,1L);
        clientServiceAdminister.countClickAndReturnUrl(1L,1L);
        clientServiceAdminister.countClickAndReturnUrl(1L,1L);
        // when
        ClientService clientService = clientServiceRepository.findClientServiceByClientAndServices(client, services).get();
        // then
        assertThat(clientService.getClickCount()).isEqualTo(8);

    }

    @Test
    @DisplayName("사용자 서비스 조회 - 기본 클릭수 정렬 ")
    public void givenClientAndServiceSearchCondition_whenSearchingFitTheConditionDefaultSortByClickNumber_thenSearchedService() throws Exception{
        // given
        List<String> categories = new ArrayList<>();
        categories.add("IT");
        ServiceSearchConditionForm serviceSearchConditionForm =
                ServiceSearchConditionForm.of(categories,null);
        // when
        Page<ClickServiceDto> popularityServiceDtos = clientServiceAdminister.servicesOfClient(1L, serviceSearchConditionForm);
        // then
        assertThat(popularityServiceDtos.getTotalElements()).isEqualTo(2);
        assertThat(popularityServiceDtos.getContent().get(0).getServiceName())
                .isEqualTo("노션");
        assertThat(popularityServiceDtos.getContent().get(1).getServiceName())
                .isEqualTo("깃허브");
    }
}