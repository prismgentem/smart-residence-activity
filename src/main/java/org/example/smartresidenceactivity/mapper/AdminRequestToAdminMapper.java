package org.example.smartresidenceactivity.mapper;

import org.example.smartresidenceactivity.config.MapperConfiguration;
import org.example.smartresidenceactivity.entity.Admin;
import org.example.smartresidenceactivity.model.reqest.AdminRequest;
import org.mapstruct.Mapper;
import jakarta.annotation.Nonnull;
import org.springframework.core.convert.converter.Converter;

@Mapper(config = MapperConfiguration.class)
public interface AdminRequestToAdminMapper extends Converter<AdminRequest, Admin> {
    @Override
    Admin convert(@Nonnull AdminRequest source);
}
