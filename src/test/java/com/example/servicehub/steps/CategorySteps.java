package com.example.servicehub.steps;

import com.example.servicehub.domain.category.Category;
import com.example.servicehub.repository.category.CategoryRepository;

public class CategorySteps {

	private final CategoryRepository categoryRepository;

	public CategorySteps(final CategoryRepository categoryRepository) {
		this.categoryRepository = categoryRepository;
	}

	public Category create(final String categoryName) {
		return categoryRepository.save(Category.of(categoryName));
	}
}
