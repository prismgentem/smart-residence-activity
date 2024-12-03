package org.example.smartresidenceactivity.mapper;

import org.example.smartresidenceactivity.config.MapperConfiguration;
import org.example.smartresidenceactivity.entity.Residence;
import org.example.smartresidenceactivity.model.reqest.ResidenceRequest;
import org.mapstruct.Mapper;
import jakarta.annotation.Nonnull;
import org.springframework.core.convert.converter.Converter;

@Mapper(config = MapperConfiguration.class)
public interface ResidenceRequestToResidenceMapper extends Converter<ResidenceRequest, Residence> {
    @Override
    Residence convert(@Nonnull ResidenceRequest source);
}
