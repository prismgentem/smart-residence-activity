package org.example.mainservice.mapper.residence;

import org.example.mainservice.config.MapperConfiguration;
import org.example.mainservice.entity.Residence;
import org.example.mainservice.model.reqest.ResidenceRequest;
import org.mapstruct.Mapper;
import jakarta.annotation.Nonnull;
import org.mapstruct.MappingTarget;
import org.springframework.core.convert.converter.Converter;

@Mapper(config = MapperConfiguration.class)
public interface ResidenceRequestToResidenceMapper extends Converter<ResidenceRequest, Residence> {
    @Override
    Residence convert(@Nonnull ResidenceRequest source);

    Residence toResidenceForUpdate(@MappingTarget Residence target, ResidenceRequest source);
}
