package org.example.smartresidenceactivity.mapper;

import org.example.smartresidenceactivity.config.MapperConfiguration;
import org.example.smartresidenceactivity.entity.User;
import org.example.smartresidenceactivity.model.reqest.UserRequest;
import org.mapstruct.Mapper;
import jakarta.annotation.Nonnull;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.core.convert.converter.Converter;

@Mapper(config = MapperConfiguration.class)
public interface UserRequestToUserMapper extends Converter<UserRequest, User> {
    @Override
    User convert(@Nonnull UserRequest source);

    User toUserForUpdate(@MappingTarget User target, UserRequest source);
}
