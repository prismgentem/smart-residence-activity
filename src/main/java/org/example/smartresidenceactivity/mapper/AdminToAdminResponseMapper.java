package org.example.smartresidenceactivity.mapper;

import org.example.smartresidenceactivity.config.MapperConfiguration;
import org.example.smartresidenceactivity.entity.Admin;
import org.example.smartresidenceactivity.model.response.AdminResponse;
import org.mapstruct.Mapper;
import jakarta.annotation.Nonnull;
import org.springframework.core.convert.converter.Converter;

@Mapper(config = MapperConfiguration.class)
public interface AdminToAdminResponseMapper extends Converter<Admin, AdminResponse> {
    @Override
    AdminResponse convert(@Nonnull Admin source);
}
