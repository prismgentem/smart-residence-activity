package org.example.mainservice.scheduler;

import lombok.RequiredArgsConstructor;
import org.example.mainservice.entity.Subscriber;
import org.example.mainservice.model.response.EventResponse;
import org.example.mainservice.repository.SubscriberRepository;
import org.example.mainservice.service.EventService;
import org.example.mainservice.service.NotificationService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WeeklyNotificationScheduler {
    private final NotificationService notificationService;
    private final SubscriberRepository subscriberRepository;
    private final EventService eventService;

    @Scheduled(cron = "${scheduling.weekly-notification.cron}", zone = "${scheduling.weekly-notification.timezone}")
    @Transactional(readOnly = true)
    public void sendWeeklyNotifications() {
        var subscribers = subscriberRepository.findAll();

        Map<UUID, List<Subscriber>> subscribersByResidence = subscribers.stream()
                .collect(Collectors.groupingBy(Subscriber::getResidenceId));

        subscribersByResidence.forEach((residenceId, residenceSubscribers) -> {
            var events = eventService.getEventsNearResidence(residenceId);
            if (!events.isEmpty()) {
                residenceSubscribers.forEach(subscriber -> sendNotification(subscriber, events));
            }
        });
    }

    private void sendNotification(Subscriber subscriber, List<EventResponse> events) {
        notificationService.sendEventNearResidenceEmailNotificationMessage(subscriber.getTelegramUsername(), subscriber.getEmail(), events);
    }
}

