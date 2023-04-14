package com.example.servicehub.repository.requestService;

import com.example.servicehub.domain.requestService.RequestServiceArticle;
import com.example.servicehub.domain.requestService.RequestStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RequestServiceArticleRepository extends JpaRepository<RequestServiceArticle, Long> {
    Page<RequestServiceArticle> findRequestServiceArticleByRequestStatus(RequestStatus requestStatus, Pageable pageable);
}
