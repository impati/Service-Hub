package com.example.servicehub.service.impl;

import com.example.servicehub.config.StepsConfig;
import com.example.servicehub.config.TestJpaConfig;
import com.example.servicehub.domain.CustomerService;
import com.example.servicehub.domain.Services;
import com.example.servicehub.service.ServiceClickCounter;
import com.example.servicehub.steps.CustomerServiceSteps;
import com.example.servicehub.steps.ServicesSteps;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

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
    public void given_when_then() throws Exception {
        // given
        Long clientId = 1L;
        Services services = servicesSteps.create();
        int click = 10;
        CustomerService customerService = customerServiceSteps.createWithClick(clientId, services, click);
        // when
        String redirectUrl = serviceClickCounter.countClickAndReturnUrl(clientId, services.getId());
        // then
        Assertions.assertThat(redirectUrl).isEqualTo(services.getServiceUrl());
        Assertions.assertThat(customerService.getClickCount()).isEqualTo(click + 1);
    }
}