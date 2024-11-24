package org.example.emailnotificationservice.model.kudago;

import lombok.Data;

import java.util.List;

@Data
public class KudagoResponse {
    private int count;
    private String next;
    private String previous;
    private List<Event> results;
}
