package com.example.servicehub.service;


import com.example.servicehub.config.TestJpaConfig;
import com.example.servicehub.domain.ServiceCategory;
import com.example.servicehub.domain.Services;
import com.example.servicehub.dto.ServicesRegisterForm;
import com.example.servicehub.repository.CategoryRepository;
import com.example.servicehub.repository.ServiceCategoryRepository;
import com.example.servicehub.repository.ServicesRepository;
import com.example.servicehub.service.fileUtils.LogoManager;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.mock.web.MockMultipartFile;

import java.io.FileInputStream;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;


@DisplayName("서비스 등록 테스트")
@DataJpaTest
@Import({TestJpaConfig.class,ServicesRegister.class, LogoManager.class})
class ServicesRegisterTest {

    @Autowired private ServicesRegister servicesRegister;
    @Autowired private ServicesRepository servicesRepository;
    @Autowired private ServiceCategoryRepository serviceCategoryRepository;

    @Test
    @DisplayName("서비스 등록 테스트")
    public void given_when_then() throws Exception{
        // given
        List<String> categoryList = List.of("IT","REPO");
        String serviceUrl = "https://www.inflearn.com/";
        String content = "hi 인프런";
        MockMultipartFile logo = new MockMultipartFile("inflearn-logo",
                "test.png",
                "image/png",
                new FileInputStream("/Users/jun-yeongchoe/Desktop/project/ServiceHub/src/main/resources/image/inflearn-logo.png"));

        ServicesRegisterForm servicesRegisterForm =
                ServicesRegisterForm.of(categoryList,serviceUrl,content,logo);
        // when
        servicesRegister.registerServices(servicesRegisterForm);
        // then
        Services services = servicesRepository.findByServiceUrl(serviceUrl).orElseThrow(ChangeSetPersister.NotFoundException::new);
        assertThat(services.getServiceUrl()).isEqualTo(serviceUrl);
        List<ServiceCategory> serviceCategories = serviceCategoryRepository.findByServices(services);
        serviceCategories
                .stream()
                .forEach(serviceCategory -> {
                    assertThat(serviceCategory.getCategory().getName()).containsAnyOf("IT","REPO");
                });
    }
}