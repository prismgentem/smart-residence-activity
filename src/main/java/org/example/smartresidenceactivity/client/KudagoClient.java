package org.example.smartresidenceactivity.client;

import lombok.RequiredArgsConstructor;
import org.example.smartresidenceactivity.model.kudago.KudagoEventQueryParams;
import org.example.smartresidenceactivity.model.kudago.KudagoEventResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Component
@RequiredArgsConstructor
public class KudagoClient {
    private final WebClient webClient;

    public Mono<KudagoEventResponse> getEventsNearResidence(KudagoEventQueryParams queryParams) {

        var now = Instant.now();
        var untilDate = now.plus(10, ChronoUnit.DAYS);

        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/events/")
                        .queryParam("page", queryParams.getPage() != null ? queryParams.getPage() : 1)
                        .queryParam("page_size", queryParams.getPageSize() != null ? queryParams.getPageSize() : 20)
                        .queryParam("actual_since", now.getEpochSecond())
                        .queryParam("actual_until", untilDate.getEpochSecond())
                        .queryParam("lon", queryParams.getLon()) //долгота
                        .queryParam("lat", queryParams.getLat()) //широта
                        .queryParam("radius", queryParams.getRadius())
                        .queryParam("fields", "dates,title,description,age_restriction,price,images,id,place,age_restriction")
                        .queryParam("expand", "place")
                        .build())
                .retrieve()
                .bodyToMono(KudagoEventResponse.class);
    }
}
