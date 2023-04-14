package com.example.servicehub.repository.services;

import com.example.servicehub.domain.services.ServiceComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ServiceCommentRepository extends JpaRepository<ServiceComment, Long> {


    @Query("select cs from ServiceComment cs  where cs.services.id = :serviceId")
    List<ServiceComment> findByServices(@Param("serviceId") Long serviceId);

    List<ServiceComment> findByCustomerId(Long customerId);

    @Query("select count(sc) > 0 from ServiceComment  sc " +
            " where sc.customerId = :customerId " +
            " and sc = :comment")
    boolean existsByCustomer(@Param("comment") ServiceComment comment, @Param("customerId") Long customer);
}
