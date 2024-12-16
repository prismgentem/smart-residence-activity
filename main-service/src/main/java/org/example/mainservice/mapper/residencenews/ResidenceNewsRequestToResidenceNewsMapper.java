package org.example.mainservice.mapper.residencenews;

import jakarta.annotation.Nonnull;
import org.example.mainservice.config.MapperConfiguration;
import org.example.mainservice.entity.ResidenceNews;
import org.example.mainservice.mapper.residence.ShortResidenceRequestToResidenceMapper;
import org.example.mainservice.model.reqest.ResidenceNewsRequest;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.springframework.core.convert.converter.Converter;

@Mapper(config = MapperConfiguration.class, uses = {ShortResidenceRequestToResidenceMapper.class})
public interface ResidenceNewsRequestToResidenceNewsMapper extends Converter<ResidenceNewsRequest, ResidenceNews> {
    @Override
    ResidenceNews convert(@Nonnull ResidenceNewsRequest source);

    ResidenceNews toResidenceNewsForUpdate(@MappingTarget ResidenceNews target, ResidenceNewsRequest source);
}
