package com.toy.overall_practice.api.category;

import com.toy.overall_practice.domain.post.region.Region;
import com.toy.overall_practice.service.category.CategoryService;
import com.toy.overall_practice.service.category.dto.CategoryDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class CategoryRestController {

    private final CategoryService categoryService;

    @GetMapping("/categories/{name}")
    public ResponseEntity<List<CategoryDto>> getCategories(@PathVariable String name) {
        List<CategoryDto> childCategories = categoryService.findChildCategories(name);
        return ResponseEntity.ok().body(childCategories);
    }

    @GetMapping("/regions")
    public ResponseEntity<List<String>> getRegions() {
        Region[] values = Region.values();
        List<String> regions = Arrays.stream(values).map(Region::getCategory).collect(Collectors.toList());
        return ResponseEntity.ok().body(regions);
    }
}
