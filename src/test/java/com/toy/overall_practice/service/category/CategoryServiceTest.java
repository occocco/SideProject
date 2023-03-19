package com.toy.overall_practice.service.category;

import com.toy.overall_practice.domain.category.Category;
import com.toy.overall_practice.domain.category.repository.CategoryRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
class CategoryServiceTest {

    @Mock
    CategoryRepository categoryRepository;
    @InjectMocks
    CategoryService categoryService;

    @Test
    void findCategoryTest(){

        String categoryName = "의류";
        Category category = new Category(1L, categoryName, null, new ArrayList<>(), new ArrayList<>());

        when(categoryRepository.findByName(categoryName)).thenReturn(Optional.of(category));

        Category result = categoryService.findCategory(categoryName);

        assertThat(result).isEqualTo(category);
    }

    @Test
    void findCategoryExTest() {

        String categoryName = "잘못된 카테고리";

        when(categoryRepository.findByName(categoryName)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> categoryService.findCategory(categoryName));

    }
}