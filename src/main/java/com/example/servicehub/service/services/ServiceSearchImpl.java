package com.example.servicehub.service.services;

import static java.util.stream.Collectors.*;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.servicehub.dto.services.PopularityServiceDto;
import com.example.servicehub.dto.services.ServiceSearchConditionForm;
import com.example.servicehub.repository.customer.CustomerServiceRepository;
import com.example.servicehub.repository.services.ServicesRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ServiceSearchImpl implements ServiceSearch {

	private final ServicesRepository servicesRepository;
	private final CustomerServiceRepository customerServiceRepository;

	@Override
	public Page<PopularityServiceDto> search(
		final ServiceSearchConditionForm condition,
		final Optional<Long> optionalCustomer,
		final Pageable pageRequest
	) {
		final Page<PopularityServiceDto> searchedService = servicesRepository.search(condition.getCategories(),
			condition.getServiceName(), pageRequest);

		optionalCustomer.ifPresent(customer -> setCustomerPossessServices(customer, searchedService.getContent()));
		return searchedService;
	}

	private void setCustomerPossessServices(final Long customerId, final List<PopularityServiceDto> services) {
		List<Long> customerServices = customerServiceRepository.findServiceIdOwnedByCustomer(services.stream()
			.map(PopularityServiceDto::getServiceId)
			.collect(toList()), customerId);

		for (var service : services) {
			ifContainSetPossess(customerServices, service);
		}
	}

	private void ifContainSetPossess(final List<Long> customerServices, final PopularityServiceDto service) {
		if (customerServices.contains(service.getServiceId())) {
			service.setPossess(true);
		}
	}
}
