package com.example.servicehub.repository;

import com.example.servicehub.domain.Services;
import com.example.servicehub.repository.querydsl.ServiceSearchRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ServicesRepository extends JpaRepository<Services,Long> , ServiceSearchRepository {

    Optional<Services> findByServiceUrl(String serviceUrl);

    @Query("select s from Services s " +
            " join fetch s.serviceCategories sc " +
            " join fetch sc.category c " +
            " where s.id =:serviceId")
    Optional<Services> findByIdUseFetchJoin(@Param("serviceId") Long serviceId);

}
