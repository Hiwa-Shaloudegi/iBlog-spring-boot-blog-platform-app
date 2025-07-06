package dev.hiwa.iblog.services;

import dev.hiwa.iblog.domain.dto.request.CreateCategoryRequest;
import dev.hiwa.iblog.domain.dto.response.CategoryDto;
import dev.hiwa.iblog.domain.entities.Category;
import dev.hiwa.iblog.exceptions.ResourceAlreadyExistsException;
import dev.hiwa.iblog.exceptions.ResourceConstraintViolationException;
import dev.hiwa.iblog.exceptions.ResourceNotFoundException;
import dev.hiwa.iblog.mappers.CategoryMapper;
import dev.hiwa.iblog.repositories.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;


    public List<CategoryDto> getAllCategoriesWithPosts() {
        List<Category> categories = categoryRepository.findAlWithPosts();

        return categories.stream().map(categoryMapper::toDto).toList();
    }

    @Transactional
    public CategoryDto createCategory(CreateCategoryRequest request) {
        Category category = categoryMapper.toEntity(request);

        boolean exists = categoryRepository.existsByNameIgnoreCase(category.getName());
        if (exists) {
            throw new ResourceAlreadyExistsException(Category.class.getSimpleName(),
                                                     "name",
                                                     category.getName()
            );
        }


        Category savedCategory = categoryRepository.save(category);

        return categoryMapper.toDto(savedCategory);
    }

    @Transactional
    public void deleteCategory(UUID id) {
        Category category = categoryRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(Category.class.getSimpleName(),
                                                                 "id",
                                                                 id.toString()
                ));

        if (!category.getPosts().isEmpty()) throw new ResourceConstraintViolationException(
                "Category cannot be deleted because it has associated posts.");

        categoryRepository.deleteById(id);
    }
}
