package org.example.smartresidenceactivity.mapper;

import org.example.smartresidenceactivity.config.MapperConfiguration;
import org.example.smartresidenceactivity.entity.ResidenceNews;
import org.example.smartresidenceactivity.model.reqest.ResidenceNewsRequest;
import org.mapstruct.Mapper;
import jakarta.annotation.Nonnull;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.core.convert.converter.Converter;

@Mapper(config = MapperConfiguration.class, uses = {AdminRequestToAdminMapper.class, ResidenceRequestToResidenceMapper.class})
public interface ResidenceNewsRequestToResidenceNewsMapper extends Converter<ResidenceNewsRequest, ResidenceNews> {
    @Override
    ResidenceNews convert(@Nonnull ResidenceNewsRequest source);

    ResidenceNews toResidenceNewsForUpdate(@MappingTarget ResidenceNews target, ResidenceNewsRequest source);
}
