package com.example.servicehub.service.impl;

import com.example.servicehub.config.TestJpaConfig;
import com.example.servicehub.domain.*;
import com.example.servicehub.dto.ClickServiceDto;
import com.example.servicehub.dto.ServiceSearchConditionForm;
import com.example.servicehub.repository.ClientRepository;
import com.example.servicehub.repository.ClientServiceRepository;
import com.example.servicehub.repository.CustomServiceRepository;
import com.example.servicehub.repository.ServicesRepository;
import com.example.servicehub.service.ClientServiceAdminister;
import com.example.servicehub.support.DefaultImageResizer;
import com.example.servicehub.support.JsoupMetaDataCrawler;
import com.example.servicehub.support.LogoManager;
import com.example.servicehub.util.ProjectUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName(" 사용자 서비스 기능 ")
@DataJpaTest
@Import({TestJpaConfig.class , ClientServiceAdministerImpl.class,
        CustomServiceAdministerImpl.class, JsoupMetaDataCrawler.class,
        LogoManager.class, DefaultImageResizer.class
})
class ClientServiceAdministerImplTest {

    @Autowired private ClientServiceAdminister clientServiceAdminister;
    @Autowired private ClientRepository clientRepository;
    @Autowired private ClientServiceRepository clientServiceRepository;
    @Autowired private ServicesRepository servicesRepository;
    @Autowired private CustomServiceRepository customServiceRepository;

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
        clientServiceAdminister.deleteClientService(1L,1L,false);
        clientServiceAdminister.deleteClientService(1L,1L,false);
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
        String serviceUrl = clientServiceAdminister.countClickAndReturnUrl(1L,1L,false);
        clientServiceAdminister.countClickAndReturnUrl(1L,1L,false);
        clientServiceAdminister.countClickAndReturnUrl(1L,1L,false);
        // when
        ClientService clientService = clientServiceRepository.findClientServiceByClientAndServices(client, services).get();
        // then
        assertThat(clientService.getClickCount()).isEqualTo(8);
        assertThat(serviceUrl).isEqualTo("https://notion.so/");

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
        List<ClickServiceDto> popularityServiceDtos = clientServiceAdminister.servicesOfClient(1L, serviceSearchConditionForm);
        // then
        assertThat(popularityServiceDtos.get(0).getServiceName())
                .isEqualTo("노션");
        assertThat(popularityServiceDtos.get(1).getServiceName())
                .isEqualTo("깃허브");
    }

    @Test
    @DisplayName("사용자 서비스 조회 - 커스텀 서비스와 함께 조회 - 커스텀 서비스 먼저")
    public void givenClientAndServiceSearchCondition_whenSearchingCustomServiceFirstDefaultSortByClickNumber_thenSearchedService() throws Exception{
        // given
        Client client = clientRepository.findById(1L).get();
        List<String> categories = new ArrayList<>();
        ServiceSearchConditionForm serviceSearchConditionForm =
                ServiceSearchConditionForm.of(categories,"");

        addCustomService(client,"test");
        addCustomService(client,"service");
        addCustomService(client,"sql");
        // when
        List<ClickServiceDto> clickServiceDtos = clientServiceAdminister.servicesOfClient(client.getId(), serviceSearchConditionForm);
        // then

        assertThat(clickServiceDtos.size())
                .isEqualTo(clientServiceRepository.findServiceByClientId(client.getId()).size() + 3);

        assertThat(clickServiceDtos.subList(0,3).stream().map(ClickServiceDto::getServiceName).collect(Collectors.toList()))
                .containsAnyOf("test","service","sql");
    }

    @Test
    @DisplayName("사용자 서비스 조회 - 커스텀 서비스와 함께 조회 - 커스텀 서비스를 먼저 조회 - 서비스 이름 필터 추가")
    public void givenClientAndServiceSearchCondition_whenSearchingCustomServiceFirstDefaultSortByClickNumber_thenSearchedServiceVersion2() throws Exception{
        // given
        Client client = clientRepository.findById(1L).get();
        List<String> categories = new ArrayList<>();
        ServiceSearchConditionForm serviceSearchConditionForm =
                ServiceSearchConditionForm.of(categories,"test");

        addCustomService(client,"test");
        addCustomService(client,"service");
        addCustomService(client,"sql");
        // when
        List<ClickServiceDto> clickServiceDtos = clientServiceAdminister.servicesOfClient(client.getId(), serviceSearchConditionForm);
        // then

        assertThat(clickServiceDtos.size())
                .isEqualTo(1);

        assertThat(clickServiceDtos.get(0).getServiceName())
                .isEqualTo("test");
    }

    @Test
    @DisplayName("사용자 서비스 조회 - 카테고리")
    public void givenClientAndServiceSearchCondition_whenSearchingCustomServiceFirstDefaultSortByClickNumber_thenSearchWithEachCategories() throws Exception{
        // given
        Client client = clientRepository.findById(1L).get();
        ServiceSearchConditionForm serviceSearchConditionForm = ServiceSearchConditionForm.of(new ArrayList<>(List.of("CUSTOM")),null);
        // when
        addCustomService(client,"test");
        List<ClickServiceDto> clickServiceDtos = clientServiceAdminister.servicesOfClient(client.getId(), serviceSearchConditionForm);
        // then
        assertThat(clickServiceDtos.size())
                .isEqualTo(1);

        assertThat(clickServiceDtos.get(0).getCategories())
                .isEqualTo("CUSTOM");
    }


    private void addCustomService(Client client,String serviceName){
        customServiceRepository.save(
                CustomService.builder()
                        .serviceUrl("https://test.com")
                        .client(client)
                        .logoStoreName(UUID.randomUUID().toString())
                        .serviceUrl("")
                        .title("")
                        .serviceName(serviceName)
                        .build());
    }
}