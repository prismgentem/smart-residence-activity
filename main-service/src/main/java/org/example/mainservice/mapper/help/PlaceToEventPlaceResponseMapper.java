package org.example.mainservice.mapper.help;

import jakarta.annotation.Nonnull;
import org.example.mainservice.config.MapperConfiguration;
import org.example.mainservice.model.kudago.Place;
import org.example.mainservice.model.response.EventPlaceResponse;
import org.mapstruct.Mapper;
import org.springframework.core.convert.converter.Converter;

@Mapper(config = MapperConfiguration.class)
public interface PlaceToEventPlaceResponseMapper extends Converter<Place, EventPlaceResponse> {
    @Override
    EventPlaceResponse convert(@Nonnull Place source);
}
