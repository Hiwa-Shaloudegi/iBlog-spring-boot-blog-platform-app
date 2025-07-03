package dev.hiwa.iblog.services;

import dev.hiwa.iblog.domain.dto.CategoryDto;
import dev.hiwa.iblog.domain.entities.Category;
import dev.hiwa.iblog.mappers.CategoryMapper;
import dev.hiwa.iblog.repositories.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;


    public List<CategoryDto> getAllCategoriesWithPosts() {
        List<Category> categories = categoryRepository.findAlWithPosts();

        return categories.stream().map(categoryMapper::toDto).toList();
    }
}
