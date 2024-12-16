package org.example.mainservice.mapper.residence;

import org.example.mainservice.config.MapperConfiguration;
import org.example.mainservice.entity.Residence;
import org.example.mainservice.model.response.ResidenceResponse;
import org.mapstruct.Mapper;
import jakarta.annotation.Nonnull;
import org.springframework.core.convert.converter.Converter;
@Mapper(config = MapperConfiguration.class)
public interface ResidenceToResidenceResponseMapper extends Converter <Residence, ResidenceResponse> {
    @Override
    ResidenceResponse convert(@Nonnull Residence source);
}
