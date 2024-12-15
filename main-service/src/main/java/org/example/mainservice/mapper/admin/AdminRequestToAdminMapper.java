package org.example.mainservice.mapper.admin;

import org.example.mainservice.config.MapperConfiguration;
import org.example.mainservice.entity.Admin;
import org.example.mainservice.model.reqest.AdminRequest;
import org.mapstruct.Mapper;
import jakarta.annotation.Nonnull;
import org.mapstruct.MappingTarget;
import org.springframework.core.convert.converter.Converter;

@Mapper(config = MapperConfiguration.class)
public interface AdminRequestToAdminMapper extends Converter<AdminRequest, Admin> {
    @Override
    Admin convert(@Nonnull AdminRequest source);

    Admin toAdminForUpdate(@MappingTarget Admin target, AdminRequest source);
}
