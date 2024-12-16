package org.example.mainservice.mapper.user;

import jakarta.annotation.Nonnull;
import org.example.mainservice.entity.User;
import org.example.mainservice.config.MapperConfiguration;
import org.example.mainservice.model.keycloak.KeycloakUser;
import org.mapstruct.Mapper;
import org.springframework.core.convert.converter.Converter;
@Mapper(config = MapperConfiguration.class)

public interface UserToKeycloakUserMapper extends Converter<User, KeycloakUser> {
    @Override
    KeycloakUser convert(@Nonnull User source);
}
