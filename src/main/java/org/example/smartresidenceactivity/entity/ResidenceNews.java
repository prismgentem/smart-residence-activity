package org.example.smartresidenceactivity.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;
import org.example.smartresidenceactivity.entity.base.BaseEntityCU;

@Getter
@Setter
@Accessors(chain = true)
@SuperBuilder
@NoArgsConstructor
@ToString(callSuper = true)
@Entity
@Table(name = "residence_news")
public class ResidenceNews extends BaseEntityCU {

    @Column(name = "title")
    private String title;

    @Column(name = "content")
    private String content;

    @ManyToOne
    @JoinColumn(name = "created_by", referencedColumnName = "id")
    @ToString.Exclude
    private Admin createdBy;

    @ManyToOne
    @JoinColumn(name = "residence_id", nullable = false)
    private Residence residence;
}
