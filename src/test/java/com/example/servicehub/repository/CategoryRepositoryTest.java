package com.example.servicehub.repository;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import com.example.servicehub.config.TestJpaConfig;
import com.example.servicehub.domain.category.Category;
import com.example.servicehub.repository.category.CategoryRepository;

@DisplayName("Category 테스트")
@Import(TestJpaConfig.class)
@DataJpaTest
class CategoryRepositoryTest {

	@Autowired
	private CategoryRepository categoryRepository;

	@Test
	@DisplayName("카테고리 게층 구조 테스트 - parent 편")
	void givenParentCategoryAndChildList_whenSavingAndAddingChildList_thenValidParent() {
		// given
		final Category parentCategory = Category.of("ITTest");
		final Category backend = Category.of("BACKEND");
		final Category devops = Category.of("DEVOPS");
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
			.forEach(category -> assertThat("BACKEND and DEVOPS").contains(category.getName()));
	}

	@Test
	@DisplayName("카테고리 게층 구조 테스트 - child 편")
	void givenParentCategoryAndChildList_whenSavingAndAddingChildList_thenValidChild() {
		// given
		final Category parentCategory = Category.of("ITTest");
		final Category backend = Category.of("BACKEND");
		final Category devops = Category.of("DEVOPS");
		parentCategory.addChildCategory(backend);
		parentCategory.addChildCategory(devops);
		//when
		categoryRepository.save(parentCategory);
		categoryRepository.save(backend);
		categoryRepository.save(devops);
		categoryRepository.flush();
		// then
		final Category findBackend = returnCategoryAndNotFoundCheck(backend.getId());
		assertThat(findBackend.getParent()).isEqualTo(parentCategory);
	}

	private Category returnCategoryAndNotFoundCheck(final Long id) {
		assertThatCode(() -> categoryRepository.findById(id).orElseThrow()).doesNotThrowAnyException();
		return categoryRepository.findById(id).orElseThrow();
	}
}
