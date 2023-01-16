package com.example.servicehub.repository;

import com.example.servicehub.domain.Services;
import com.example.servicehub.repository.querydsl.ServiceSearchRepository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ServicesRepository extends JpaRepository<Services,Long> , ServiceSearchRepository {

    Optional<Services> findByServiceUrl(String serviceUrl);

}
