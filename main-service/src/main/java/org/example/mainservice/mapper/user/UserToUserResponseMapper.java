package org.example.mainservice.mapper.user;

import org.example.mainservice.config.MapperConfiguration;
import org.example.mainservice.entity.User;
import org.example.mainservice.mapper.residence.ResidenceToShortResidenceResponseMapper;
import org.example.mainservice.model.response.UserResponse;
import org.mapstruct.Mapper;
import jakarta.annotation.Nonnull;
import org.springframework.core.convert.converter.Converter;

@Mapper(config = MapperConfiguration.class, uses = {ResidenceToShortResidenceResponseMapper.class})
public interface UserToUserResponseMapper extends Converter<User, UserResponse> {
    @Override
    UserResponse convert(@Nonnull User source);
}
