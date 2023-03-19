package com.toy.overall_practice.domain.category.repository;

import com.toy.overall_practice.domain.category.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    Optional<Category> findByName(String name);

    List<Category> findByParentCategory(Category parentCategory);

}
