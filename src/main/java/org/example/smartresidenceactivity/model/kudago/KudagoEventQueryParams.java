package org.example.smartresidenceactivity.model.kudago;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class KudagoEventQueryParams {
    private String location;
    private String lang;
    private Integer page;
    private Integer pageSize;
    private Double lon;
    private Double lat;
    private Integer radius;
}
