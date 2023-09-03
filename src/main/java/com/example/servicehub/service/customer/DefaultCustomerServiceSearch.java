package com.example.servicehub.service.customer;

import static java.util.stream.Collectors.*;

import java.util.Comparator;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.example.servicehub.domain.custom.CustomService;
import com.example.servicehub.dto.services.ClickServiceDto;
import com.example.servicehub.dto.services.ServiceSearchConditionForm;
import com.example.servicehub.repository.customer.CustomerCustomServiceRepository;
import com.example.servicehub.repository.services.ServicesRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class DefaultCustomerServiceSearch implements CustomerServiceSearch {

	private final ServicesRepository servicesRepository;
	private final CustomerCustomServiceRepository customerCustomServiceRepository;

	@Override
	@Transactional
	public List<ClickServiceDto> servicesOfCustomer(final Long customerId, final ServiceSearchConditionForm condition) {
		return servicesRepository.searchByCustomer(customerId, condition.getCategories(), condition.getServiceName())
			.stream()
			.distinct()
			.sorted(Comparator.comparing(ClickServiceDto::getClick).reversed())
			.collect(toList());
	}

	@Override
	public List<CustomService> customServicesOfCustomer(final Long customerId, final String serviceName) {
		if (StringUtils.hasText(serviceName)) {
			return customerCustomServiceRepository.findCustomServiceByCustomerIdAndServiceName(customerId, serviceName);
		}

		return customerCustomServiceRepository.findCustomServiceByCustomerId(customerId);
	}
}

