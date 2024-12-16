package org.example.mainservice.mapper.admin;

import jakarta.annotation.Nonnull;
import org.example.mainservice.config.MapperConfiguration;
import org.example.mainservice.entity.Admin;
import org.example.mainservice.model.keycloak.KeycloakUser;
import org.mapstruct.Mapper;
import org.springframework.core.convert.converter.Converter;

@Mapper(config = MapperConfiguration.class)
public interface AdminToKeycloakUserMapper extends Converter<Admin, KeycloakUser> {
    @Override
    KeycloakUser convert(@Nonnull Admin source);
}
