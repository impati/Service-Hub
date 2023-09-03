package com.example.servicehub.service.customer;

import java.util.List;

import com.example.servicehub.domain.custom.CustomService;
import com.example.servicehub.dto.services.ClickServiceDto;
import com.example.servicehub.dto.services.ServiceSearchConditionForm;

public interface CustomerServiceSearch {

	List<ClickServiceDto> servicesOfCustomer(
		final Long customerId,
		final ServiceSearchConditionForm serviceSearchConditionForm
	);

	List<CustomService> customServicesOfCustomer(final Long customerId, final String serviceName);
}
