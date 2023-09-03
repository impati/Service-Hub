package com.example.servicehub.service.impl;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import com.example.servicehub.config.StepsConfig;
import com.example.servicehub.config.TestJpaConfig;
import com.example.servicehub.domain.customer.CustomerService;
import com.example.servicehub.domain.services.Services;
import com.example.servicehub.service.services.DefaultServiceClickCounter;
import com.example.servicehub.service.services.ServiceClickCounter;
import com.example.servicehub.steps.CustomerServiceSteps;
import com.example.servicehub.steps.ServicesSteps;

@DisplayName("서비스 클릭 테스트")
@DataJpaTest
@Import({TestJpaConfig.class, StepsConfig.class, DefaultServiceClickCounter.class})
class DefaultServiceClickCounterTest {

    @Autowired
    private ServiceClickCounter serviceClickCounter;

    @Autowired
    private CustomerServiceSteps customerServiceSteps;

    @Autowired
    private ServicesSteps servicesSteps;

    @Test
    @DisplayName("사용자가 서비스를 클릭시에 서비스 클릭이 증가한다.")
    void clickTest() {
        // given
        final Long customerId = 1L;
        final Services services = servicesSteps.create();
        final int click = 10;
        final CustomerService customerService = customerServiceSteps.createWithClick(customerId, services, click);

        // when
        final String redirectUrl = serviceClickCounter.countClickAndReturnUrl(customerId, services.getId());

        // then
        assertThat(redirectUrl).isEqualTo(services.getServiceUrl());
        assertThat(customerService.getClickCount()).isEqualTo(click + 1);
    }
}
