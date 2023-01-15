package com.example.servicehub.repository;

import com.example.servicehub.config.JpaConfig;
import com.example.servicehub.config.TestJpaConfig;
import com.example.servicehub.domain.Category;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Category 테스트")
@Import(TestJpaConfig.class)
@DataJpaTest
class CategoryRepositoryTest {

    @Autowired
    private  CategoryRepository categoryRepository;

    @Test
    @DisplayName("카테고리 게층 구조 테스트 - parent 편")
    public void givenParentCategoryAndChildList_whenSavingAndAddingChildList_thenValidParent() throws Exception{
        // given
        Category parentCategory = Category.of("ITTest");
        Category  backend = Category.of("BACKEND");
        Category devops = Category.of("DEVOPS");
        parentCategory.addChildCategory(backend);
        parentCategory.addChildCategory(devops);
        //when
        categoryRepository.save(parentCategory);
        categoryRepository.save(backend);
        categoryRepository.save(devops);
        categoryRepository.flush();
        // then
        Category findParentCategory = returnCategoryAndNotFoundCheck(parentCategory.getId());
        findParentCategory
                .getChild()
                .stream()
                .forEach(category -> assertThat("BACKEND and DEVOPS").contains(category.getName()));

    }

    @Test
    @DisplayName("카테고리 게층 구조 테스트 - child 편")
    public void givenParentCategoryAndChildList_whenSavingAndAddingChildList_thenValidChild() throws Exception{
        // given
        Category parentCategory = Category.of("ITTest");
        Category  backend = Category.of("BACKEND");
        Category devops = Category.of("DEVOPS");
        parentCategory.addChildCategory(backend);
        parentCategory.addChildCategory(devops);
        //when
        categoryRepository.save(parentCategory);
        categoryRepository.save(backend);
        categoryRepository.save(devops);
        categoryRepository.flush();
        // then
        Category findBackend = returnCategoryAndNotFoundCheck(backend.getId());
        assertThat(findBackend.getParent()).isEqualTo(parentCategory);
    }
    private Category returnCategoryAndNotFoundCheck(Long id) throws Exception{
        assertThatCode(() -> categoryRepository.findById(id).orElseThrow()).doesNotThrowAnyException();
        return categoryRepository.findById(id).orElseThrow();
    }
}