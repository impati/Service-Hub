package com.example.servicehub.repository;

import com.example.servicehub.domain.ServiceCategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ServiceCategoryRepository extends JpaRepository<ServiceCategory,Long> {
}
