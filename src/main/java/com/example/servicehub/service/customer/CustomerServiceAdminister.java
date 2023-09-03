package com.example.servicehub.service.customer;

public interface CustomerServiceAdminister {

	void addCustomerService(final Long customerId, final Long serviceId);

	void deleteCustomerService(final Long customerId, final Long serviceId);
}
