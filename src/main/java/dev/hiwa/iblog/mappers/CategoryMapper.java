package dev.hiwa.iblog.mappers;

import dev.hiwa.iblog.domain.dto.request.CreateCategoryRequest;
import dev.hiwa.iblog.domain.dto.response.CategoryDto;
import dev.hiwa.iblog.domain.entities.Category;
import dev.hiwa.iblog.domain.entities.Post;
import dev.hiwa.iblog.domain.enums.PostStatus;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CategoryMapper {


    @Mapping(target = "postCount", source = "posts", qualifiedByName = "getPostCount")
    CategoryDto toDto(Category category);

    Category toEntity(CreateCategoryRequest request);

    Category toEntity(CategoryDto request);


    @Named("getPostCount")
    default long getPostCount(List<Post> posts) {
        if (posts == null) return 0;
        return posts.stream().filter(post -> post.getStatus().equals(PostStatus.PUBLISHED)).count();
    }

}
