package com.example.servicehub.repository;

import com.example.servicehub.domain.CustomService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CustomServiceRepository extends JpaRepository<CustomService,Long> {

    @Query(" select s from CustomService s " +
            " where s.serviceUrl = :serviceUrl " +
            " and s.client.id = :clientId ")
    Optional<CustomService> findCustomServiceByServiceUrl(
            @Param("clientId") Long clientId , @Param("serviceUrl") String serviceUrl);

    @Query(" select s from CustomService s " +
            " where s.id = :serviceId " +
            " and s.client.id = :clientId ")
    Optional<CustomService> findCustomServiceByClientIdAndServiceId(
            @Param("clientId") Long clientId , @Param("serviceId") Long customServiceId);

    @Query(" select s from CustomService s " +
            " where s.client.id = :clientId")
    List<CustomService> findCustomServiceByClientId(@Param("clientId") Long clientId);

    @Query(" select s from CustomService s " +
            " where s.client.id = :clientId " +
            " and s.serviceName like concat('%',:serviceName,'%') ")
    List<CustomService> findCustomServiceByClientIdAndServiceName(
            @Param("clientId") Long clientId , @Param("serviceName") String serviceName);

}
