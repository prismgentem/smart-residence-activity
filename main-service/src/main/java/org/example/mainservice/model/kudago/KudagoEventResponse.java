package org.example.mainservice.model.kudago;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class KudagoEventResponse {
    private int count;
    private String next;
    private String previous;
    private List<Event> results;
}
