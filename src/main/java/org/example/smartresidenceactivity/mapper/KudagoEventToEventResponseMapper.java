package org.example.smartresidenceactivity.mapper;

import jakarta.annotation.Nullable;
import org.example.smartresidenceactivity.config.MapperConfiguration;
import org.example.smartresidenceactivity.model.kudago.KudagoEvent;
import org.example.smartresidenceactivity.model.response.EventDateResponse;
import org.example.smartresidenceactivity.model.response.EventResponse;
import org.mapstruct.BeforeMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.core.convert.converter.Converter;

import java.time.Instant;
import java.util.Date;

@Mapper(config = MapperConfiguration.class)
public interface KudagoEventToEventResponseMapper extends Converter<KudagoEvent, EventResponse> {
    @BeforeMapping
    default void beforeMapping(@MappingTarget EventResponse target, KudagoEvent source) {
        var dates = source.getDates().stream().map(
                date -> EventDateResponse.builder()
                        .startDate(Date.from(Instant.ofEpochSecond(date.getStart())))
                        .endDate(Date.from(Instant.ofEpochSecond(date.getEnd())))
                        .build()
        ).toList();

        target.setDates(dates);
    }

    @Override
    EventResponse convert(@Nullable KudagoEvent source);

}
