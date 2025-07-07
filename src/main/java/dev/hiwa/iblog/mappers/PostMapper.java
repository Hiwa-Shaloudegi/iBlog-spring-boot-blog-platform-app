package dev.hiwa.iblog.mappers;

import dev.hiwa.iblog.domain.dto.request.CreatePostRequest;
import dev.hiwa.iblog.domain.dto.request.UpdatePostRequest;
import dev.hiwa.iblog.domain.dto.response.PostDto;
import dev.hiwa.iblog.domain.entities.Post;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PostMapper {

    @Mapping(target = "author", source = "author")
    @Mapping(target = "category", source = "category")
    @Mapping(target = "tags", source = "tags")
    PostDto toDto(Post post);

    // ignore fields that need to be set manually
    @Mapping(target = "author", ignore = true)
    @Mapping(target = "category", ignore = true)
    @Mapping(target = "tags", ignore = true)
    @Mapping(target = "readingTime", ignore = true)
    Post toEntity(CreatePostRequest request);

    @Mapping(target = "author", ignore = true)
    @Mapping(target = "category", ignore = true)
    @Mapping(target = "tags", ignore = true)
    @Mapping(target = "readingTime", ignore = true)
    Post toEntity(UpdatePostRequest request);
}
