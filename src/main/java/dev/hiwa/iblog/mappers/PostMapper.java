package dev.hiwa.iblog.mappers;

import dev.hiwa.iblog.domain.dto.request.RegisterRequest;
import dev.hiwa.iblog.domain.dto.response.PostDto;
import dev.hiwa.iblog.domain.dto.response.UserDto;
import dev.hiwa.iblog.domain.entities.Post;
import dev.hiwa.iblog.domain.entities.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PostMapper {

    @Mapping(target = "author" ,source = "author")
    @Mapping(target = "category" ,source = "category")
    @Mapping(target = "tags" ,source = "tags")
    PostDto toDto(Post post);
}
