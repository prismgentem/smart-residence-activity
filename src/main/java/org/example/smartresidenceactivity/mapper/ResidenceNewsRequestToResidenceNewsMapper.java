package org.example.smartresidenceactivity.mapper;

import org.example.smartresidenceactivity.config.MapperConfiguration;
import org.example.smartresidenceactivity.entity.ResidenceNews;
import org.example.smartresidenceactivity.model.reqest.ResidenceNewsReqest;
import org.mapstruct.Mapper;
import jakarta.annotation.Nonnull;
import org.springframework.core.convert.converter.Converter;

@Mapper(config = MapperConfiguration.class)
public interface ResidenceNewsRequestToResidenceNewsMapper extends Converter<ResidenceNewsReqest, ResidenceNews> {
    @Override
    ResidenceNews convert(@Nonnull ResidenceNewsReqest source);
}
