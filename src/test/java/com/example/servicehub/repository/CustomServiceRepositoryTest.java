package com.example.servicehub.repository;

import com.example.servicehub.config.TestJpaConfig;
import com.example.servicehub.repository.customer.CustomerCustomServiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

@DataJpaTest
@Import(TestJpaConfig.class)
class CustomServiceRepositoryTest {

    @Autowired
    private CustomerCustomServiceRepository customerCustomServiceRepository;


}