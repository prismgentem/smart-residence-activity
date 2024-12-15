package org.example.mainservice.mapper.user;

import jakarta.annotation.Nonnull;
import org.example.mainservice.config.MapperConfiguration;
import org.example.mainservice.entity.User;
import org.example.mainservice.model.reqest.ShortUserRequest;
import org.mapstruct.Mapper;
import org.springframework.core.convert.converter.Converter;

@Mapper(config = MapperConfiguration.class)
public interface ShortUserRequestToUserMapper extends Converter<ShortUserRequest, User> {
    @Override
    User convert(@Nonnull ShortUserRequest source);
}
