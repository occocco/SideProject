package com.toy.overall_practice.service.category;

import com.toy.overall_practice.domain.category.Category;
import com.toy.overall_practice.domain.category.repository.CategoryRepository;
import com.toy.overall_practice.service.category.dto.CategoryDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public Category findCategory(String name) {
        return categoryRepository.findByName(name)
                .orElseThrow(() -> new NoSuchElementException("카테고리 정보를 찾을 수 없습니다."));
    }

    public List<CategoryDto> findChildCategories(String name) {

        Category parentCategory = categoryRepository.findByName(name)
                .orElseThrow(() -> new NoSuchElementException("부모 카테고리를 찾을 수 없습니다."));

        List<Category> childCategories = categoryRepository.findByParentCategory(parentCategory);

        return childCategories.stream().map(category -> new CategoryDto(category.getName())).collect(Collectors.toList());
    }
}
