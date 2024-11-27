package org.example.emailnotificationservice.service;

import lombok.RequiredArgsConstructor;
import org.example.emailnotificationservice.model.kudago.EventResponse;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.List;

@Service
@RequiredArgsConstructor
public class HtmlTemplateService {
    private static final String EVENTS_NEAR_RESIDENCE_TEMPLATE = "events-near-residence-template";
    private final TemplateEngine templateEngine;

    public String generateEventNearResidenceHtmlTemplate(List<EventResponse> events) {
        var context = new Context();
        context.setVariable("events", events);
        return templateEngine.process(EVENTS_NEAR_RESIDENCE_TEMPLATE, context);
    }
}
