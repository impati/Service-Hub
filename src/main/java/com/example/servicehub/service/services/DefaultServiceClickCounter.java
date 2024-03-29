package com.example.servicehub.service.services;

import javax.persistence.EntityNotFoundException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.servicehub.domain.customer.CustomerService;
import com.example.servicehub.repository.customer.CustomerServiceRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class DefaultServiceClickCounter implements ServiceClickCounter {

	private final CustomerServiceRepository customerServiceRepository;

	@Override
	public String countClickAndReturnUrl(final Long customerId, final Long serviceId) {
		final CustomerService customerService = customerServiceRepository.findCustomerServiceBy(customerId, serviceId)
			.orElseThrow(() -> new EntityNotFoundException("유효하지 않은 메서드 호출입니다."));

		customerService.click();
		return customerService.getServices().getServiceUrl();
	}
}
