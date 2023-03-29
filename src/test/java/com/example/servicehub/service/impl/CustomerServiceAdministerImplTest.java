package com.example.servicehub.service.impl;

import com.example.servicehub.config.StepsConfig;
import com.example.servicehub.config.TestJpaConfig;
import com.example.servicehub.domain.Services;
import com.example.servicehub.repository.CustomerServiceRepository;
import com.example.servicehub.service.CustomerServiceAdminister;
import com.example.servicehub.steps.ServicesSteps;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName(" 사용자 서비스 기능 ")
@DataJpaTest
@Import({TestJpaConfig.class, StepsConfig.class, CustomerServiceAdministerImpl.class})
class CustomerServiceAdministerImplTest {

    @Autowired
    private CustomerServiceAdminister customerServiceAdminister;
    @Autowired
    private CustomerServiceRepository customerServiceRepository;
    @Autowired
    private ServicesSteps servicesSteps;

    @Test
    @DisplayName("사용자 서비스 추가")
    public void givencustomerIdAndServiceId_whenAdding_thenAddServiceOfcustomer() throws Exception {
        // given
        int size = 10;
        Long customerId = 1L;
        List<Services> services = servicesSteps.creates(size);
        // when
        for (var service : services) {
            customerServiceAdminister.addCustomerService(customerId, service.getId());
        }
        // then
        List<Services> serviceOfcustomer = customerServiceRepository
                .findServiceByCustomerId(customerId);

        assertThat(serviceOfcustomer.size()).isEqualTo(size);
        assertThat(serviceOfcustomer).containsAnyElementsOf(services);
    }

    @Test
    @DisplayName("사용자 서비스 추가 중복시 무시")
    public void givencustomerAndServiceDuplication_whenOneAdding_thenOneAddServiceForcustomer() throws Exception {
        // given
        Long customerId = 55L;
        Services services = servicesSteps.create();
        // when
        customerServiceAdminister.addCustomerService(customerId, services.getId());
        customerServiceAdminister.addCustomerService(customerId, services.getId());
        customerServiceAdminister.addCustomerService(customerId, services.getId());
        // then
        assertThat(customerServiceRepository.findServiceByCustomerId(customerId).size())
                .isEqualTo(1);
    }

    @Test
    @DisplayName("사용자 서비스 삭제")
    public void givencustomerAndService_whenDeletingService_thenDelete() throws Exception {
        // given
        int size = 3;
        Long customerId = 55L;
        List<Services> services = servicesSteps.creates(size);
        for (int i = 0; i < size; i++) {
            customerServiceAdminister.addCustomerService(customerId, services.get(i).getId());
        }
        // when
        customerServiceAdminister.deleteCustomerService(customerId, services.get(0).getId());
        customerServiceAdminister.deleteCustomerService(customerId, services.get(0).getId());
        // then
        assertThat(customerServiceRepository.findServiceByCustomerId(customerId).size())
                .isEqualTo(size - 1);
    }
}