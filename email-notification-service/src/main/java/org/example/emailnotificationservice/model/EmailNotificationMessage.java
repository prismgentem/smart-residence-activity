package org.example.emailnotificationservice.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.emailnotificationservice.model.kudago.KudagoResponse;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmailNotificationMessage {
    private String email;
    private String subject;
    private String body;
    private List<KudagoResponse> events;
}
