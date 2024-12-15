package org.example.mainservice.mapper.user;

import org.example.mainservice.entity.User;
import org.example.mainservice.mapper.residence.ShortResidenceRequestToResidenceMapper;
import org.example.mainservice.config.MapperConfiguration;
import org.example.mainservice.model.reqest.UserRequest;
import org.mapstruct.Mapper;
import jakarta.annotation.Nonnull;
import org.mapstruct.MappingTarget;
import org.springframework.core.convert.converter.Converter;

@Mapper(config = MapperConfiguration.class, uses = {ShortResidenceRequestToResidenceMapper.class})
public interface UserRequestToUserMapper extends Converter<UserRequest, User> {
    @Override
    User convert(@Nonnull UserRequest source);

    User toUserForUpdate(@MappingTarget User target, UserRequest source);
}
