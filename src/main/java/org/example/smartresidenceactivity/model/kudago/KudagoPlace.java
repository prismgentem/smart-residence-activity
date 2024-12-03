package org.example.smartresidenceactivity.model.kudago;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class KudagoPlace {
    private Integer id;
    private String title;
    private String address;
    private String subway;
}