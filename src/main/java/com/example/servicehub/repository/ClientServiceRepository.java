package com.example.servicehub.repository;

import com.example.servicehub.domain.Client;
import com.example.servicehub.domain.ClientService;
import com.example.servicehub.domain.Services;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ClientServiceRepository extends JpaRepository<ClientService,Long> {

    Optional<ClientService> findClientServiceByClientAndServices(Client client , Services services);

    @Query("select count(cs) > 0 from ClientService cs where cs.services = :service and cs.client.id = :client")
    boolean existsServiceAndClientRelationship(@Param("service")Services services , @Param("client") Long clientId);

    @Query("select cs.services from ClientService cs where cs.client.id = :clientId ")
    List<Services> findServiceByClientId(@Param("clientId") Long clientId);

    @Query("select count(cs) > 0 from ClientService cs " +
            " where cs.client = :client " +
            " and cs.services = :service ")
    boolean alreadyExistsServiceForClient(@Param("client") Client client, @Param("service") Services services);
}
