package com.example.servicehub.repository.customer;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.servicehub.domain.custom.CustomService;

public interface CustomerCustomServiceRepository extends JpaRepository<CustomService, Long> {
    
	@Query(" select s from CustomService s " +
		" where s.id = :serviceId " +
		" and s.customerId = :customerId ")
	Optional<CustomService> findCustomServiceByCustomerIdAndServiceId(
		@Param("customerId") final Long customerId,
		@Param("serviceId") final Long customServiceId
	);

	@Query(" select s from CustomService s " +
		" where s.customerId = :customerId")
	List<CustomService> findCustomServiceByCustomerId(@Param("customerId") final Long customerId);

	@Query(" select s from CustomService s " +
		" where s.customerId = :customerId " +
		" and s.serviceName like concat('%',:serviceName,'%') ")
	List<CustomService> findCustomServiceByCustomerIdAndServiceName(
		@Param("customerId") final Long customerId,
		@Param("serviceName") final String serviceName
	);
}
