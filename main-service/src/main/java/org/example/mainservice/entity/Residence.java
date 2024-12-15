package org.example.mainservice.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.example.mainservice.entity.base.BaseEntityCU;

import java.util.List;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@ToString(callSuper = true)
@Entity
@Table(name = "residence")
public class Residence extends BaseEntityCU {

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "address", nullable = false)
    private String address;

    @Column(name = "lon", nullable = false)
    private Double lon;

    @Column(name = "lat", nullable = false)
    private Double lat;

    @Column(name = "management_company")
    private String managementCompany;

    @ToString.Exclude
    @OneToMany(mappedBy = "residence")
    private List<Admin> admins;

    @ToString.Exclude
    @OneToMany(mappedBy = "residence",fetch = FetchType.LAZY)
    private List<ResidenceNews> newsList;
}
