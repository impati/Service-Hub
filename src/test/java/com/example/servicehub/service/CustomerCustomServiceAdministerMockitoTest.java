package com.example.servicehub.service;

import com.example.servicehub.domain.CustomService;
import com.example.servicehub.dto.CustomServiceForm;
import com.example.servicehub.repository.CustomerCustomServiceRepository;
import com.example.servicehub.service.impl.CustomerCustomServiceAdministerImpl;
import com.example.servicehub.support.LogoManager;
import com.example.servicehub.support.MetaDataCrawler;
import com.example.servicehub.support.ServiceMetaData;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.BDDMockito.*;

@DisplayName("사용자 커스텀 서비스")
@ExtendWith(MockitoExtension.class)
class CustomerCustomServiceAdministerMockitoTest {

    @InjectMocks
    private CustomerCustomServiceAdministerImpl customServiceAdminister;

    @Mock
    private MetaDataCrawler metaDataCrawler;

    @Mock
    private LogoManager logoManager;

    @Mock
    private CustomerCustomServiceRepository customerServiceRepository;

    @Test
    @DisplayName("커스텀 서비스 등록")
    public void givenClientIdAndServiceUrl_whenRegisterCustomServiceForCustomer_thenRegisterCustomServiceForCustomer() throws Exception {
        // given
        Long clientId = 1L;
        String serviceUrl = "https://service-hub.org";
        CustomServiceForm customServiceForm = CustomServiceForm.builder()
                .serviceName("서비스허브")
                .serviceUrl(serviceUrl)
                .build();

        ServiceMetaData serviceMetaData = new ServiceMetaData(serviceUrl);
        String storeName = "unique.png";

        given(metaDataCrawler.tryToGetMetaData(customServiceForm.getServiceUrl()))
                .willReturn(serviceMetaData);

        given(logoManager.download(serviceMetaData.getImage()))
                .willReturn(storeName);

        CustomService customService = create(serviceMetaData, storeName, customServiceForm, clientId);

        BDDMockito.given(customerServiceRepository.save(ArgumentMatchers.refEq(customService)))
                .willReturn(customService);
        // when

        customServiceAdminister.addCustomService(clientId, customServiceForm);
        // then
        then(customerServiceRepository).should()
                .save(ArgumentMatchers.refEq(customService));
    }

    @Test
    @DisplayName("커스텀 서비스 삭제")
    public void given_when_then() throws Exception {
        // given
        Long customerId = 1L;
        Long customServiceId = 1L;
        CustomService customService = create(customerId);
        given(customerServiceRepository.findCustomServiceByClientIdAndServiceId(customerId, customServiceId))
                .willReturn(Optional.of(customService));
        willDoNothing().given(customerServiceRepository).delete(customService);
        // when
        customServiceAdminister.deleteCustomService(customerId, customServiceId);
        // then
        then(customerServiceRepository).should().delete(customService);

    }

    private CustomService create(Long customerId) {
        return CustomService.builder()
                .serviceName("test")
                .logoStoreName("default.png")
                .serviceUrl("https://test.com")
                .clientId(customerId)
                .build();
    }

    private CustomService create(ServiceMetaData serviceMetaData, String logoStoreName, CustomServiceForm request, Long clientId) {
        return CustomService.builder()
                .serviceName(request.getServiceName())
                .serviceUrl(request.getServiceUrl())
                .title(serviceMetaData.getTitle())
                .logoStoreName(logoStoreName)
                .clientId(clientId)
                .build();
    }
}
