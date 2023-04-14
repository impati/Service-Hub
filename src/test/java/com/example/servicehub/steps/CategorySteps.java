package com.example.servicehub.steps;

import com.example.servicehub.domain.category.Category;
import com.example.servicehub.repository.category.CategoryRepository;

import java.util.List;

import static java.util.stream.Collectors.toList;

public class CategorySteps {

    private final CategoryRepository categoryRepository;

    public CategorySteps(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public Category create(String categoryName) {
        return categoryRepository.save(Category.of(categoryName));
    }

    public List<Category> creates(List<String> categoryNames) {
        return categoryRepository.saveAll(categoryNames.stream()
                .map(Category::of)
                .collect(toList()));
    }

}
