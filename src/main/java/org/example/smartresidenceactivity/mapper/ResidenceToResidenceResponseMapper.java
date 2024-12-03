package org.example.smartresidenceactivity.mapper;

import org.example.smartresidenceactivity.config.MapperConfiguration;
import org.example.smartresidenceactivity.entity.Residence;
import org.example.smartresidenceactivity.model.response.ResidenceResponse;
import org.mapstruct.Mapper;
import jakarta.annotation.Nonnull;
import org.springframework.core.convert.converter.Converter;
@Mapper(config = MapperConfiguration.class)
public interface ResidenceToResidenceResponseMapper extends Converter <Residence, ResidenceResponse> {
    @Override
    ResidenceResponse convert(@Nonnull Residence source);
}
