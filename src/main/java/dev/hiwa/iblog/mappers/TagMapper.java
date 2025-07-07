package dev.hiwa.iblog.mappers;

import dev.hiwa.iblog.domain.dto.response.TagDto;
import dev.hiwa.iblog.domain.entities.Post;
import dev.hiwa.iblog.domain.entities.Tag;
import dev.hiwa.iblog.domain.enums.PostStatus;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;

import java.util.Set;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TagMapper {

    @Mapping(target = "posts", ignore = true)
    Tag toEntity(TagDto tagDto);


    @Mapping(target = "postCount", source = "posts", qualifiedByName = "getPostCount")
    TagDto toDto(Tag tag);

    @Named("getPostCount")
    default Long getPostCount(Set<Post> posts) {
        if (posts == null) return null;
        return posts.stream().filter(post -> post.getStatus().equals(PostStatus.PUBLISHED)).count();
    }

}
