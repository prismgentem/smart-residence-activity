package org.example.telegrambotnotificationservice.model.kudago;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventPlaceResponse {
    private String title;
    private String address;
    private String phone;
    private String subway;
    private String siteUrl;
}
