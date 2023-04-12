package com.toy.overall_practice.api.category;

import com.toy.overall_practice.domain.post.region.Region;
import com.toy.overall_practice.service.category.CategoryService;
import com.toy.overall_practice.service.category.dto.CategoryDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Api(value = "카테고리 REST API", tags = {"Category REST API"})
@RestController
@RequiredArgsConstructor
public class CategoryRestController {

    private final CategoryService categoryService;

    @ApiOperation(value = "하위 카테고리 조회", notes = "상위 카테고리 값으로 하위 카테고리 검색하는 API")
    @GetMapping("/categories/{parentCategory}")
    public ResponseEntity<List<CategoryDto>> getCategories(@PathVariable String parentCategory) {
        List<CategoryDto> childCategories = categoryService.findChildCategories(parentCategory);
        return ResponseEntity.ok().body(childCategories);
    }

    @ApiOperation(value = "거래 지역 조회", notes = "DB에 저장된 지역 조회 API")
    @GetMapping("/regions")
    public ResponseEntity<List<String>> getRegions() {
        Region[] values = Region.values();
        List<String> regions = Arrays.stream(values).map(Region::getCategory).collect(Collectors.toList());
        return ResponseEntity.ok().body(regions);
    }
}
