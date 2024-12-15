package org.example.mainservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.mainservice.exception.ErrorType;
import org.example.mainservice.exception.ServiceException;
import org.example.mainservice.repository.ResidenceRepository;
import org.example.mainservice.client.KudagoClient;
import org.example.mainservice.entity.Residence;
import org.example.mainservice.model.kudago.KudagoEventQueryParams;
import org.example.mainservice.model.response.EventResponse;
import org.example.mainservice.util.ErrorMessageConstants;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class EventService {
    private final KudagoClient kudagoClient;
    private final ConversionService conversionService;
    private final ResidenceRepository residenceRepository;

    public List<EventResponse> getEventsNearResidence(UUID residenceId) {
        var residence = residenceRepository.findById(residenceId).orElseThrow(() ->
                new ServiceException(ErrorType.BAD_REQUEST, String.format(ErrorMessageConstants.MSG_RESIDENCE_NOT_FOUND, residenceId))
        );
        var queryParams = createDefaultQueryParamsForEventsNearResidence(residence);

        var response = Optional.ofNullable(kudagoClient.getEventsNearResidence(queryParams).block())
                .orElseThrow(() ->
                        new ServiceException(ErrorType.SERVICE_UNAVAILABLE, ErrorMessageConstants.MSG_GET_EVENTS_FROM_KUDAGO_NOT_AVAILABLE)
                );

        return response.getResults().stream().map(
                event -> conversionService.convert(event, EventResponse.class)
        ).toList();
    }

    private KudagoEventQueryParams createDefaultQueryParamsForEventsNearResidence(Residence residence) {
        return KudagoEventQueryParams.builder()
                .lang("ru")
                .page(1)
                .pageSize(10)
                .lat(residence.getLat())
                .lon(residence.getLon())
                .radius(3000)
                .build();
    }
}
