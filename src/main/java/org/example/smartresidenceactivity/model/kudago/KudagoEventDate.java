package org.example.smartresidenceactivity.model.kudago;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class KudagoEventDate {
    private Long start;
    private Long end;
}
