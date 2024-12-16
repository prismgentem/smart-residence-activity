package org.example.mainservice.mapper.user;

import jakarta.annotation.Nonnull;
import org.example.mainservice.config.MapperConfiguration;
import org.example.mainservice.entity.User;
import org.example.mainservice.model.response.ShortUserResponse;
import org.mapstruct.Mapper;
import org.springframework.core.convert.converter.Converter;

@Mapper(config = MapperConfiguration.class)
public interface UserToShortUserResponseMapper extends Converter<User, ShortUserResponse> {
    @Override
    ShortUserResponse convert(@Nonnull User source);
}
