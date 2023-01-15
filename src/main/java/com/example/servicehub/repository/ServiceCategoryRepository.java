package com.example.servicehub.repository;

import com.example.servicehub.domain.ServiceCategory;
import com.example.servicehub.domain.Services;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ServiceCategoryRepository extends JpaRepository<ServiceCategory,Long> {

    List<ServiceCategory> findByServices(Services services);

}
