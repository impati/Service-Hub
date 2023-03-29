package com.example.servicehub.repository;

import com.example.servicehub.domain.CustomService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CustomerCustomServiceRepository extends JpaRepository<CustomService, Long> {

    @Query(" select s from CustomService s " +
            " where s.serviceUrl = :serviceUrl " +
            " and s.customerId = :customerId ")
    Optional<CustomService> findCustomServiceByServiceUrl(
            @Param("customerId") Long customerId, @Param("serviceUrl") String serviceUrl);

    @Query(" select s from CustomService s " +
            " where s.id = :serviceId " +
            " and s.customerId = :customerId ")
    Optional<CustomService> findCustomServiceByCustomerIdAndServiceId(
            @Param("customerId") Long customerId, @Param("serviceId") Long customServiceId);

    @Query(" select s from CustomService s " +
            " where s.customerId = :customerId")
    List<CustomService> findCustomServiceByCustomerId(@Param("customerId") Long customerId);

    @Query(" select s from CustomService s " +
            " where s.customerId = :customerId " +
            " and s.serviceName like concat('%',:serviceName,'%') ")
    List<CustomService> findCustomServiceByCustomerIdAndServiceName(
            @Param("customerId") Long customerId, @Param("serviceName") String serviceName);

}
