package org.example.smartresidenceactivity.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;
import org.example.smartresidenceactivity.entity.base.BaseEntityCU;

import java.util.List;

@Getter
@Setter
@Accessors(chain = true)
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

    @OneToMany(mappedBy = "residence")
    private List<ResidenceNews> newsList;
}
