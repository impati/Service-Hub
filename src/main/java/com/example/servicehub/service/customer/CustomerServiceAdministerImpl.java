package com.example.servicehub.service.customer;

import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.servicehub.domain.customer.CustomerService;
import com.example.servicehub.domain.services.Services;
import com.example.servicehub.repository.customer.CustomerServiceRepository;
import com.example.servicehub.repository.services.ServicesRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class CustomerServiceAdministerImpl implements CustomerServiceAdminister {

	private final CustomerServiceRepository customerServiceRepository;
	private final ServicesRepository servicesRepository;

	@Override
	public void addCustomerService(final Long customerId, final Long serviceId) {
		final Services services = findService(serviceId);
		if (NotExistForCustomer(customerId, services)) {
			customerServiceRepository.save(CustomerService.of(customerId, services));
		}
	}

	private Services findService(final Long serviceId) {
		return servicesRepository.findById(serviceId)
			.orElseThrow(() -> new EntityNotFoundException("유효하지 않은 서비스입니다."));
	}

	private boolean NotExistForCustomer(final Long customerId, final Services services) {
		return !customerServiceRepository.alreadyExistsServiceForCustomer(customerId, services);
	}

	@Override
	public void deleteCustomerService(final Long customerId, final Long serviceId) {
		final Optional<CustomerService> optionalCustomerService = customerServiceRepository.findCustomerServiceBy(
			customerId,
			serviceId
		);

		optionalCustomerService.ifPresent(customerServiceRepository::delete);
	}
}
