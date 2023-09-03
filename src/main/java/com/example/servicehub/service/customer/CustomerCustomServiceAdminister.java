package com.example.servicehub.service.customer;

import com.example.servicehub.dto.custom.CustomServiceForm;

public interface CustomerCustomServiceAdminister {

	void addCustomService(final Long customerId, final CustomServiceForm customServiceForm);

	void deleteCustomService(final Long customerId, final Long customServiceId);

	String countClickAndReturnUrl(final Long customerId, final Long customServiceId);
}
