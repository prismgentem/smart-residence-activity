package org.example.mainservice.mapper.residence;

import jakarta.annotation.Nonnull;
import org.example.mainservice.config.MapperConfiguration;
import org.example.mainservice.entity.Residence;
import org.example.mainservice.model.reqest.ShortResidenceRequest;
import org.mapstruct.Mapper;
import org.springframework.core.convert.converter.Converter;

@Mapper(config = MapperConfiguration.class)
public interface ShortResidenceRequestToResidenceMapper extends Converter<ShortResidenceRequest, Residence> {
    @Override
    Residence convert(@Nonnull ShortResidenceRequest source);
}
