package org.example.smartresidenceactivity.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.example.smartresidenceactivity.entity.base.BaseEntityCU;
import org.example.smartresidenceactivity.enums.ServiceType;
import org.example.smartresidenceactivity.enums.Status;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@ToString(callSuper = true)
@Entity
@Table(name = "service_request")
public class ServiceRequest extends BaseEntityCU {

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @ToString.Exclude
    private User user;

    @Column(name = "service_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private ServiceType serviceType;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private Status status;

    @Column(name = "description")
    private String description;
}
