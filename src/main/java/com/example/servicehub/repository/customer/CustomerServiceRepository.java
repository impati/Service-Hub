package com.example.servicehub.repository.customer;

import com.example.servicehub.domain.customer.CustomerService;
import com.example.servicehub.domain.services.Services;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CustomerServiceRepository extends JpaRepository<CustomerService, Long> {

    @Query(" select cs from CustomerService cs " +
            " join fetch cs.services s " +
            " where cs.customerId = :customerId " +
            " and s.id = :serviceId")
    Optional<CustomerService> findCustomerServiceBy(@Param("customerId") Long customerId, @Param("serviceId") Long serviceId);

    @Query("select cs.services.id from CustomerService cs where cs.services.id in (:services) and cs.customerId = :customerId")
    List<Long> findServiceIdOwnedByCustomer(@Param("services") List<Long> services, @Param("customerId") Long customerId);

    @Query("select cs.services from CustomerService cs where cs.customerId = :customerId ")
    List<Services> findServiceByCustomerId(@Param("customerId") Long customerId);

    @Query("select count(cs) > 0 from CustomerService cs " +
            " where cs.customerId = :customerId " +
            " and cs.services = :service ")
    boolean alreadyExistsServiceForCustomer(@Param("customerId") Long customerId, @Param("service") Services services);

    @Query("select count(cs) > 0 from CustomerService cs where cs.services.id = :service and cs.customerId = :customerId")
    boolean existsServiceAndCustomerRelationship(@Param("service") Long serviceId, @Param("customerId") Long customerId);

}
