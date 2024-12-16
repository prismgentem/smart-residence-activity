package org.example.mainservice.mapper.help;

import jakarta.annotation.Nonnull;
import org.example.mainservice.config.MapperConfiguration;
import org.example.mainservice.model.kudago.Event;
import org.example.mainservice.model.kudago.Image;
import org.example.mainservice.model.response.EventResponse;
import org.mapstruct.BeforeMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.core.convert.converter.Converter;

@Mapper(config = MapperConfiguration.class, uses = {PlaceToEventPlaceResponseMapper.class, EventDateToEventDateResponseMapper.class})
public interface EventToEventResponseMapper extends Converter<Event, EventResponse> {
    @BeforeMapping
    default void beforeMapping(@MappingTarget EventResponse target, Event source) {
        var images = source.getImages().stream()
                .map(Image::getImage)
                .toList();
        target.setImages(images);
    }
    @Override
    @Mapping(target = "images", ignore = true)
    EventResponse convert(@Nonnull Event source);
}
