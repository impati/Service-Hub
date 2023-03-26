package com.example.servicehub.repository;

import com.example.servicehub.domain.CustomerService;
import com.example.servicehub.domain.Services;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CustomerServiceRepository extends JpaRepository<CustomerService, Long> {

    @Query(" select cs from CustomerService cs " +
            " join fetch cs.services s " +
            " where cs.clientId = :clientId " +
            " and s.id = :serviceId")
    Optional<CustomerService> findClientServiceBy(@Param("clientId") Long clientId, @Param("serviceId") Long serviceId);

    @Query("select cs.services.id from CustomerService cs where cs.services.id in (:services) and cs.clientId = :clientId")
    List<Long> findServiceIdOwnedByClient(@Param("services") List<Long> services, @Param("clientId") Long clientId);

    @Query("select cs.services from CustomerService cs where cs.clientId = :clientId ")
    List<Services> findServiceByClientId(@Param("clientId") Long clientId);

    @Query("select count(cs) > 0 from CustomerService cs " +
            " where cs.clientId = :clientId " +
            " and cs.services = :service ")
    boolean alreadyExistsServiceForClient(@Param("clientId") Long clientId, @Param("service") Services services);

    @Query("select count(cs) > 0 from CustomerService cs where cs.services.id = :service and cs.clientId = :clientId")
    boolean existsServiceAndClientRelationship(@Param("service") Long serviceId, @Param("clientId") Long clientId);

}
