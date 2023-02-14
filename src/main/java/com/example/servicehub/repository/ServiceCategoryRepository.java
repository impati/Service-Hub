package com.example.servicehub.repository;

import com.example.servicehub.domain.ServiceCategory;
import com.example.servicehub.domain.Services;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ServiceCategoryRepository extends JpaRepository<ServiceCategory,Long> {

    List<ServiceCategory> findByServices(Services services);

    @Query("select c.name from ServiceCategory sc " +
            " join sc.services s " +
            " join sc.category c " +
            " where s.serviceName =:serviceName ")
    List<String> findByServiceName(@Param("serviceName") String serviceName);

}
