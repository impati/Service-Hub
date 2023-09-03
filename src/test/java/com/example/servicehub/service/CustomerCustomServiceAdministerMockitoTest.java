package com.example.servicehub.service;

import static org.mockito.BDDMockito.*;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.servicehub.domain.custom.CustomService;
import com.example.servicehub.dto.custom.CustomServiceForm;
import com.example.servicehub.repository.customer.CustomerCustomServiceRepository;
import com.example.servicehub.service.customer.CustomerCustomServiceAdministerImpl;
import com.example.servicehub.support.crawl.MetaDataCrawler;
import com.example.servicehub.support.crawl.ServiceMetaData;
import com.example.servicehub.support.file.LogoManager;

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
	void givencustomerIdAndServiceUrl_whenRegisterCustomServiceForCustomer_thenRegisterCustomServiceForCustomer() {
		// given
		final Long customerId = 1L;
		final String serviceUrl = "https://service-hub.org";
		final CustomServiceForm customServiceForm = CustomServiceForm.builder()
			.serviceName("서비스허브")
			.serviceUrl(serviceUrl)
			.build();
		final ServiceMetaData serviceMetaData = new ServiceMetaData(serviceUrl);
		final String storeName = "unique.png";

		given(metaDataCrawler.tryToGetMetaData(customServiceForm.getServiceUrl()))
			.willReturn(serviceMetaData);

		given(logoManager.download(serviceMetaData.getImage()))
			.willReturn(storeName);

		final CustomService customService = create(serviceMetaData, customServiceForm, customerId);

		given(customerServiceRepository.save(ArgumentMatchers.refEq(customService))).willReturn(customService);

		// when
		customServiceAdminister.addCustomService(customerId, customServiceForm);

		// then
		then(customerServiceRepository).should()
			.save(ArgumentMatchers.refEq(customService));
	}

	@Test
	@DisplayName("커스텀 서비스 삭제")
	void deleteCustomService() {
		// given
		final Long customerId = 1L;
		final Long customServiceId = 1L;
		final CustomService customService = create(customerId);
		given(customerServiceRepository.findCustomServiceByCustomerIdAndServiceId(customerId, customServiceId))
			.willReturn(Optional.of(customService));
		willDoNothing().given(customerServiceRepository).delete(customService);

		// when
		customServiceAdminister.deleteCustomService(customerId, customServiceId);

		// then
		then(customerServiceRepository).should().delete(customService);

	}

	private CustomService create(final Long customerId) {
		return CustomService.builder()
			.serviceName("test")
			.logoStoreName("default.png")
			.serviceUrl("https://test.com")
			.customerId(customerId)
			.build();
	}

	private CustomService create(
		final ServiceMetaData serviceMetaData,
		final CustomServiceForm request,
		final Long customerId
	) {
		return CustomService.builder()
			.serviceName(request.getServiceName())
			.serviceUrl(request.getServiceUrl())
			.title(serviceMetaData.getTitle())
			.logoStoreName("unique.png")
			.customerId(customerId)
			.build();
	}
}
