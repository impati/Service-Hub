package com.example.servicehub.repository;

import com.example.servicehub.domain.Client;
import com.example.servicehub.domain.ServiceComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ServiceCommentRepository extends JpaRepository<ServiceComment,Long> {


    @Query("select cs from ServiceComment cs  where cs.services.id = :serviceId")
    List<ServiceComment> findByServices(@Param("serviceId") Long serviceId);

    List<ServiceComment> findByClient(Client client);

    @Query("select count(sc) > 0 from ServiceComment  sc " +
            " where sc.client.id = :clientId " +
            " and sc = :comment")
    boolean existsByClient(@Param("comment") ServiceComment  comment , @Param("clientId") Long client);
}
