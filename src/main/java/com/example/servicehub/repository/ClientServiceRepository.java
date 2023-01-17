package com.example.servicehub.repository;

import com.example.servicehub.domain.ClientService;
import com.example.servicehub.domain.Services;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ClientServiceRepository extends JpaRepository<ClientService,Long> {

    @Query("select count(cs) > 0 from ClientService cs where cs.services = :service and cs.client.id = :client")
    boolean existsServiceAndClientRelationship(@Param("service")Services services , @Param("client") Long clientId);
}
