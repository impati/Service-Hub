package com.example.servicehub.repository;

import com.example.servicehub.domain.Client;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ClientRepository extends JpaRepository<Client,Long> {

    Optional<Client> findByUserId(String userId);

}
