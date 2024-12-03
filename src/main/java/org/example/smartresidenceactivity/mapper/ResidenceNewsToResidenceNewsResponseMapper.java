package org.example.smartresidenceactivity.mapper;

import org.example.smartresidenceactivity.config.MapperConfiguration;
import org.example.smartresidenceactivity.entity.ResidenceNews;
import org.example.smartresidenceactivity.model.response.ResidenceNewsResponse;
import org.mapstruct.Mapper;
import jakarta.annotation.Nonnull;
import org.springframework.core.convert.converter.Converter;

@Mapper(config = MapperConfiguration.class)
public interface ResidenceNewsToResidenceNewsResponseMapper extends Converter<ResidenceNews, ResidenceNewsResponse> {
    @Override
    ResidenceNewsResponse convert(@Nonnull ResidenceNews source);
}
