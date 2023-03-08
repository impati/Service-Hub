package com.example.servicehub.service;

import com.example.servicehub.config.TestJpaConfig;
import com.example.servicehub.domain.Client;
import com.example.servicehub.domain.CustomService;
import com.example.servicehub.domain.ProviderType;
import com.example.servicehub.dto.CustomServiceForm;
import com.example.servicehub.repository.ClientRepository;
import com.example.servicehub.repository.CustomServiceRepository;
import com.example.servicehub.service.impl.CustomServiceAdministerImpl;
import com.example.servicehub.support.DefaultImageResizer;
import com.example.servicehub.support.JsoupMetaDataCrawler;
import com.example.servicehub.support.LogoManager;
import com.example.servicehub.support.MetaDataCrawler;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.as;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Import({TestJpaConfig.class, JsoupMetaDataCrawler.class,
        CustomServiceAdministerImpl.class, LogoManager.class, DefaultImageResizer.class})
class CustomServiceAdministerTest {

    @Autowired
    private CustomServiceAdminister customServiceAdminister;

    @Autowired
    private CustomServiceRepository customServiceRepository;

    @Test
    @DisplayName("사용자가 커스텀한 서비스를 등록한다.")
    public void giveClientIdAndServiceUrl_whenAddingCustomServiceOfClient_thenAddCustomServiceOfClient() throws Exception{
       // given
       Long clientId  = 1L;
       String serviceUrl = "https://notion.so";

       // when

        addCustomService(clientId,serviceUrl,"twitch.tv");

       // then
       assertThat(customServiceRepository.findCustomServiceByServiceUrl(clientId,serviceUrl))
               .isPresent();

       assertThat(customServiceRepository.findCustomServiceByServiceUrl(clientId,serviceUrl).get().getServiceUrl())
               .isEqualTo(serviceUrl);

    }

    @Test
    @DisplayName("사용자가 등록한 커스텀한 서비스를 삭제한다.")
    public void givenClientIdAndCustomServiceId_whenDeletingCustomServiceOfClient_thenDeleteCustomService() throws Exception{
        // given
        Long clientId = 1L;
        String serviceUrl = "https://twitch.tv";
        addCustomService(clientId,serviceUrl,"twitch.tv");

        CustomService customService = customServiceRepository
                .findCustomServiceByServiceUrl(clientId,serviceUrl).get();

        // when

        customServiceAdminister.deleteCustomService(clientId,customService.getId());

        // then

        assertThat(customServiceRepository.findCustomServiceByServiceUrl(clientId,serviceUrl))
                .isEmpty();

    }

    @Test
    @DisplayName("사용자가 등록하지 않은 커스텀 서비스를 삭제할 수 없다")
    public void givenOtherClientIdAndCustomServiceId_whenNotDeletingCustomServiceOfClient_thenNotDeleteCustomService() throws Exception{
        // given
        Long clientId = 1L;
        String serviceUrl = "https://twitch.tv";
        addCustomService(clientId,serviceUrl,"twitch.tv");

        CustomService customService = customServiceRepository.findCustomServiceByServiceUrl(clientId,serviceUrl).get();
        // when

        customServiceAdminister.deleteCustomService(2L,customService.getId());

        // then

        assertThat(customServiceRepository.findCustomServiceByServiceUrl(clientId,serviceUrl))
                .isPresent();
    }

    @Test
    @DisplayName("사용자가 등록한 커스텀한 서비스를 모두 가져온다")
    public void givenClientId_whenSearchingCustomServicesOfClient_thenSearchCustomServiceOfClient() throws Exception{
        // given
        Long clientId = 1L;
        String firstServiceUrl = "https://twitch.tv";
        String secondServiceUrl = "https://op.gg";

        addCustomService(clientId,firstServiceUrl,"op.gg");
        addCustomService(clientId,secondServiceUrl,"twitch.tv");

        // when

        List<CustomService> customServices = customServiceAdminister.customServicesOfClient(1L,"");

        // then

        List<String> then = customServices
                .stream()
                .map(CustomService::getServiceUrl)
                .collect(toList());


        assertThat(then.size())
                .isEqualTo(2);

        assertThat(then).containsAnyOf(firstServiceUrl,secondServiceUrl);

    }

    @Test
    @DisplayName("사용자가 등록한 커스텀 서비스를 이름으로 검색한 결과를 모두 가져온다.")
    public void givenClientIdAndServiceName_whenSearchingCustomServicesOfClient_thenSearchCustomServiceOfClient() throws Exception{
        // given

        Long clientId = 1L;
        String firstServiceUrl = "https://twitch.tv";
        String secondServiceUrl = "https://op.gg";
        addCustomService(clientId,firstServiceUrl,"OP.gg");
        addCustomService(clientId,secondServiceUrl,"twitch.tv");

        // when

        List<CustomService> customServices = customServiceAdminister.customServicesOfClient(1L,"OP");

        // then

        List<String> then = customServices
                .stream()
                .map(CustomService::getServiceUrl).collect(toList());


        assertThat(then.size())
                .isEqualTo(1);

        assertThat(then).containsAnyOf(firstServiceUrl);
    }


    @Test
    @DisplayName("사용자가 등록한 커스텀한 서비스를 이용하는 경우 클릭 수를 업데이트한다.")
    public void givenClientIdAndCustomServiceId_whenUpdateClick_thenUpdateClick() throws Exception{
        // given
        Long clientId = 1L;
        String serviceUrl = "https://twitch.tv";
        addCustomService(clientId,serviceUrl,"twitch.tv");

        CustomService customService = customServiceRepository.findCustomServiceByServiceUrl(clientId,serviceUrl).get();

        // when

        String url = null;
        for(int i = 0;i<10;i++){
            url = clickCustomService(clientId,customService.getId());
        }

        CustomService updatedCustomService = customServiceRepository.findCustomServiceByServiceUrl(clientId,serviceUrl).get();
        // then

        assertThat(url).isEqualTo(serviceUrl);

        assertThat(updatedCustomService.getClickCount())
                .isEqualTo(10L);

    }


    private String clickCustomService(Long clientId,Long customServiceId){
        String url;
        url = customServiceAdminister.countClickAndReturnUrl(clientId,customServiceId);
        customServiceRepository.flush();
        return url;
    }

    private void addCustomService(Long clientId ,  String serviceUrl,String serviceName){

        customServiceAdminister.addCustomService(clientId,new CustomServiceForm(serviceName,serviceUrl));

        customServiceRepository.flush();
    }

}
