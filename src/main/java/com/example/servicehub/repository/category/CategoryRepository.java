package com.example.servicehub.repository.category;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.servicehub.domain.category.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {

	@Query("select c from Category c where c.name in(:categories)")
	List<Category> findByNames(@Param("categories") final List<String> categories);
}
