package com.example.servicehub.repository;

import com.example.servicehub.domain.ServiceComment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ServiceCommentRepository extends JpaRepository<ServiceComment,Long> {
}
