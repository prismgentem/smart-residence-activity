package org.example.mainservice.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.example.mainservice.entity.base.BaseEntityCU;

@Getter
@Setter
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

    @Column(name = "send_notification", nullable = false)
    private Boolean sendNotification;
}
