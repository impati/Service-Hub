package com.example.servicehub.repository;

import com.example.servicehub.domain.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category,Long> {

    @Query("select c from Category c where c.name in(:categories)")
    List<Category> findByNames(@Param("categories") List<String> categories);

}
