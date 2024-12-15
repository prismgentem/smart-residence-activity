package org.example.mainservice.mapper.help;

import jakarta.annotation.Nonnull;
import org.example.mainservice.config.MapperConfiguration;
import org.example.mainservice.model.kudago.EventDate;
import org.example.mainservice.model.response.EventDateResponse;
import org.mapstruct.Mapper;
import org.springframework.core.convert.converter.Converter;

@Mapper(config = MapperConfiguration.class)
public interface EventDateToEventDateResponseMapper extends Converter<EventDate, EventDateResponse> {
    @Override
    EventDateResponse convert(@Nonnull EventDate source);
}
