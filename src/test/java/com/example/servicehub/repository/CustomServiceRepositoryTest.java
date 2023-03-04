package com.example.servicehub.repository;

import com.example.servicehub.config.TestJpaConfig;
import com.example.servicehub.domain.Client;
import com.example.servicehub.domain.CustomService;
import com.example.servicehub.support.ServiceMetaData;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Import(TestJpaConfig.class)
class CustomServiceRepositoryTest {

    @Autowired
    private CustomServiceRepository customServiceRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Test
    @DisplayName("findCustomServiceByServiceUrl 쿼리 테스트")
    public void givenClientIdAndServiceUrl_when_thenFindingCustomService() throws Exception{
        // given
        String serviceUrl = "https://twitch.tv";
        Long clientId = 1L;

        Client client = clientRepository.findById(clientId).get();

        customServiceRepository.save(dummyCreateCustomServiceFrom(serviceUrl,client));

        // when
        Optional<CustomService> customService = customServiceRepository.findCustomServiceByServiceUrl(clientId, serviceUrl);

        // then
        assertThat(customService)
                .isPresent();

    }



    private CustomService dummyCreateCustomServiceFrom(String serviceUrl , Client client){
        return CustomService.builder()
                .serviceName("test")
                .serviceUrl(serviceUrl)
                .title("test")
                .logoStoreName("test")
                .client(client)
                .build();
    }

}