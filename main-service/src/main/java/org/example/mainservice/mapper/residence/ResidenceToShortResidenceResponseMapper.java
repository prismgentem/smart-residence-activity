package org.example.mainservice.mapper.residence;

import jakarta.annotation.Nonnull;
import org.example.mainservice.config.MapperConfiguration;
import org.example.mainservice.entity.Residence;
import org.example.mainservice.model.response.ShortResidenceResponse;
import org.mapstruct.Mapper;
import org.springframework.core.convert.converter.Converter;

@Mapper(config = MapperConfiguration.class)
public interface ResidenceToShortResidenceResponseMapper extends Converter<Residence, ShortResidenceResponse> {
    @Override
    ShortResidenceResponse convert(@Nonnull Residence source);
}
