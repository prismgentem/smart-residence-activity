package org.example.smartresidenceactivity.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.example.smartresidenceactivity.entity.base.BaseEntityCU;

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

    @Column(name = "management_company")
    private String managementCompany;

    @OneToMany(mappedBy = "residence")
    private List<Admin> admins;

    @OneToMany(mappedBy = "residence",fetch = FetchType.LAZY)
    private List<ResidenceNews> newsList;
}
