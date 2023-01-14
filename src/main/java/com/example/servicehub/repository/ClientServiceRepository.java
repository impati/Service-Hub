package com.example.servicehub.repository;

import com.example.servicehub.domain.ClientService;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientServiceRepository extends JpaRepository<ClientService,Long> {
}
