package com.example.servicehub.repository.services;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.servicehub.domain.services.ServiceComment;

public interface ServiceCommentRepository extends JpaRepository<ServiceComment, Long> {

	@Query("select cs from ServiceComment cs  where cs.services.id = :serviceId")
	List<ServiceComment> findByServices(@Param("serviceId") final Long serviceId);

	@Query("select count(sc) > 0 from ServiceComment  sc " +
		" where sc.customerId = :customerId " +
		" and sc = :comment")
	boolean existsByCustomer(
		@Param("comment") final ServiceComment comment,
		@Param("customerId") final Long customer
	);
}
