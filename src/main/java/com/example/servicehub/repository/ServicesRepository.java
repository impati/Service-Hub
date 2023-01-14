package com.example.servicehub.repository;

import com.example.servicehub.domain.Services;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ServicesRepository extends JpaRepository<Services,Long> {
}
