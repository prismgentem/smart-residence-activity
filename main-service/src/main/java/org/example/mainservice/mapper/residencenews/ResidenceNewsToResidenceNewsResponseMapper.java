package org.example.mainservice.mapper.residencenews;

import org.example.mainservice.config.MapperConfiguration;
import org.example.mainservice.entity.ResidenceNews;
import org.example.mainservice.model.response.ResidenceNewsResponse;
import org.mapstruct.Mapper;
import jakarta.annotation.Nonnull;
import org.springframework.core.convert.converter.Converter;

@Mapper(config = MapperConfiguration.class)
public interface ResidenceNewsToResidenceNewsResponseMapper extends Converter<ResidenceNews, ResidenceNewsResponse> {
    @Override
    ResidenceNewsResponse convert(@Nonnull ResidenceNews source);
}
