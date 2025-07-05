package dev.hiwa.iblog.mappers;

import dev.hiwa.iblog.domain.dto.request.RegisterRequest;
import dev.hiwa.iblog.domain.dto.response.UserDto;
import dev.hiwa.iblog.domain.entities.User;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {

    User toEntity(RegisterRequest request);

    UserDto toDto(User user);
}
