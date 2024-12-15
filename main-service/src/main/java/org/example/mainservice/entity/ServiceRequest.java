package org.example.mainservice.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.example.mainservice.entity.base.BaseEntityCU;
import org.example.mainservice.enums.ServiceCategory;
import org.example.mainservice.enums.ServiceType;
import org.example.mainservice.enums.Status;

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

    @ManyToOne
    @JoinColumn(name = "residence_id", referencedColumnName = "id")
    @ToString.Exclude
    private Residence residence;

    @Column(name = "service_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private ServiceType serviceType;

    @Column(name = "service_category", nullable = false)
    @Enumerated(EnumType.STRING)
    private ServiceCategory serviceCategory;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private Status status;

    @Column(name = "description")
    private String description;
}
