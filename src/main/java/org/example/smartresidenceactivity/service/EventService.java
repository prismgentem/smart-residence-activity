package org.example.smartresidenceactivity.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.smartresidenceactivity.client.KudagoClient;
import org.example.smartresidenceactivity.exception.ErrorType;
import org.example.smartresidenceactivity.exception.ServiceException;
import org.example.smartresidenceactivity.model.kudago.KudagoEventQueryParams;
import org.example.smartresidenceactivity.model.response.EventResponse;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class EventService {
    private final KudagoClient kudagoClient;
    private final ConversionService conversionService;

    //TODO брать из jwt юзера искать его резеданцию что бы искать новости возле него
    public List<EventResponse> getEventsNearResidence(KudagoEventQueryParams queryParams) {
        var response = Optional.ofNullable(kudagoClient.getEventsNearResidence(queryParams).block())
                .orElseThrow(() -> {
                    log.error("Failed to fetch events near residence from Kudago");
                    return new ServiceException(ErrorType.SERVICE_UNAVAILABLE, "Не удалось получить данные о событиях рядом");
                });

        return response.getEvents().stream().map(
                event -> conversionService.convert(event, EventResponse.class)
        ).toList();
    }


}
