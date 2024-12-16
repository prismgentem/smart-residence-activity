package org.example.mainservice.mapper.admin;

import org.example.mainservice.config.MapperConfiguration;
import org.example.mainservice.entity.Admin;
import org.example.mainservice.model.response.AdminResponse;
import org.mapstruct.Mapper;
import jakarta.annotation.Nonnull;
import org.springframework.core.convert.converter.Converter;

@Mapper(config = MapperConfiguration.class)
public interface AdminToAdminResponseMapper extends Converter<Admin, AdminResponse> {
    @Override
    AdminResponse convert(@Nonnull Admin source);
}
