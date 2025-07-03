package dev.hiwa.iblog.controllers;

import dev.hiwa.iblog.domain.dto.request.CreateCategoryRequest;
import dev.hiwa.iblog.domain.dto.response.CategoryDto;
import dev.hiwa.iblog.services.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/categories")
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping
    public ResponseEntity<List<CategoryDto>> getAllCategories() {
        List<CategoryDto> categories = categoryService.getAllCategoriesWithPosts();

        return ResponseEntity.ok(categories);
    }

    @PostMapping
    public ResponseEntity<CategoryDto> createCategory(
            @Valid @RequestBody CreateCategoryRequest request, UriComponentsBuilder uriBuilder
    ) {
        CategoryDto categoryDto = categoryService.createCategory(request);

        var uri = uriBuilder.path("/api/v1/categories/{id}").buildAndExpand(categoryDto.getId()).toUri();

        return ResponseEntity.created(uri).body(categoryDto);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable("id") UUID id) {
        categoryService.deleteCategory(id);

        return ResponseEntity.noContent().build();

    }
}
