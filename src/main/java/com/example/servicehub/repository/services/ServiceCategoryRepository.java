package com.example.servicehub.repository.services;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.servicehub.domain.services.ServiceCategory;
import com.example.servicehub.domain.services.Services;

public interface ServiceCategoryRepository extends JpaRepository<ServiceCategory, Long> {

	List<ServiceCategory> findByServices(final Services services);
}
