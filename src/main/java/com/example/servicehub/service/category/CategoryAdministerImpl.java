package com.example.servicehub.service.category;

import com.example.servicehub.domain.category.Category;
import com.example.servicehub.repository.category.CategoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CategoryAdministerImpl implements CategoryAdminister {

    private final CategoryRepository categoryRepository;

    @Override
    public List<String> getAllCategories() {
        return categoryRepository.findAll()
                .stream()
                .map(Category::getName)
                .collect(Collectors.toList());
    }

}
