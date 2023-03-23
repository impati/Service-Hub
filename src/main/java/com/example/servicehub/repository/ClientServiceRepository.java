package com.example.servicehub.repository;

import com.example.servicehub.domain.ClientService;
import com.example.servicehub.domain.Services;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ClientServiceRepository extends JpaRepository<ClientService, Long> {

    Optional<ClientService> findClientServiceByClientIdAndServices(Long clientId, Services services);

    @Query("select cs.services.id from ClientService cs where cs.services.id in (:services) and cs.clientId = :clientId")
    List<Long> findServiceIdOwnedByClient(@Param("services") List<Long> services, @Param("clientId") Long clientId);

    @Query("select cs.services from ClientService cs where cs.clientId = :clientId ")
    List<Services> findServiceByClientId(@Param("clientId") Long clientId);

    @Query("select count(cs) > 0 from ClientService cs " +
            " where cs.clientId = :clientId " +
            " and cs.services = :service ")
    boolean alreadyExistsServiceForClient(@Param("clientId") Long clientId, @Param("service") Services services);

    @Query("select count(cs) > 0 from ClientService cs where cs.services.id = :service and cs.clientId = :clientId")
    boolean existsServiceAndClientRelationship(@Param("service") Long serviceId, @Param("clientId") Long clientId);

}
