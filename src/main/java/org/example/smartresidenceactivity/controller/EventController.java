package org.example.smartresidenceactivity.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.example.smartresidenceactivity.model.response.EventResponse;
import org.example.smartresidenceactivity.service.EventService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/event")
@Api(value = "Event Controller", tags = {"Event API"})
public class EventController {
    private final EventService eventService;
    @ApiOperation(value = "Ближайшие к ЖК события и мероприятия, которые приходят из KudaGo")
    @GetMapping()
    public ResponseEntity<EventResponse> getEvents() {
        //TODO добавить логику выдачи событий
        return null;
    }
}
