package org.example.smartresidenceactivity.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.example.smartresidenceactivity.model.response.EventResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/event")
@Api(value = "Event Controller", tags = {"Event API"})
public class EventController {
    @ApiOperation(value = "События и мероприятия в городе, которые приходят из KudaGo")
    @GetMapping()
    public ResponseEntity<EventResponse> getEvents() {
        return null;
    }
}
