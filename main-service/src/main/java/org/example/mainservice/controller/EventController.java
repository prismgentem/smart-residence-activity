package org.example.mainservice.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.example.mainservice.kafka.NotificationProducer;
import org.example.mainservice.model.notification.EventNearResidenceEmailNotificationMessage;
import org.example.mainservice.model.response.EventResponse;
import org.example.mainservice.service.EventService;
import org.example.mainservice.service.SubscriberService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/event")
@SecurityRequirement(name = "Keycloak")
public class EventController {
    private final EventService eventService;
    private final SubscriberService subscriberService;
    private final NotificationProducer notificationProducer;

    @GetMapping("/{residenceId}")
    public ResponseEntity<List<EventResponse>> getEventsNearResidence(@PathVariable UUID residenceId) {
        return ResponseEntity.ok(eventService.getEventsNearResidence(residenceId));
    }

    @PostMapping("/subscribe")
    public ResponseEntity<Void> subscribeToEventsNearResidence() {
        subscriberService.subscribeToEventsNearResidence();
        return ResponseEntity.ok().build();
    }

    @PostMapping("/unsubscribe")
    public ResponseEntity<Void> unsubscribeToEventsNearResidence() {
        subscriberService.unsubscribeToEventsNearResidence();
        return ResponseEntity.ok().build();
    }

    @GetMapping("/test-email-notification")
    public ResponseEntity<List<EventResponse>> testEmailNotification() {
        var events = eventService.getEventsNearResidence(UUID.fromString("1c771d0a-6c93-4c1b-88f0-f83ab3ab79c1"));

        notificationProducer.sendEmailNotification(EventNearResidenceEmailNotificationMessage.builder()
                .body("тестовое тело сообщения")
                .email("foksfire12345px@gmail.com")
                .telegramUsername("orhemy")
                .type("event_near_residence")
                .subject("Подборка событий рядом с вашим домом")
                .events(events)
                .build());
        return ResponseEntity.ok(events);
    }

}
