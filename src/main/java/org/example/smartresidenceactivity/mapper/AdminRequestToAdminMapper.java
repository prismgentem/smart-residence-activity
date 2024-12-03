package org.example.smartresidenceactivity.mapper;

import org.example.smartresidenceactivity.config.MapperConfiguration;
import org.example.smartresidenceactivity.entity.Admin;
import org.example.smartresidenceactivity.model.reqest.AdminRequest;
import org.mapstruct.Mapper;
import jakarta.annotation.Nonnull;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.core.convert.converter.Converter;

@Mapper(config = MapperConfiguration.class)
public interface AdminRequestToAdminMapper extends Converter<AdminRequest, Admin> {
    @Override
    Admin convert(@Nonnull AdminRequest source);

    Admin toAdminForUpdate(@MappingTarget Admin target, AdminRequest source);
}
