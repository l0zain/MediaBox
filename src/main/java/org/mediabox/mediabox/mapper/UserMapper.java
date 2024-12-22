package org.mediabox.mediabox.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mediabox.mediabox.dto.user.UserRegisterDto;
import org.mediabox.mediabox.entity.User;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {
    User toEntity(UserRegisterDto userRegisterDto);

    UserRegisterDto toDto(User user);
}
