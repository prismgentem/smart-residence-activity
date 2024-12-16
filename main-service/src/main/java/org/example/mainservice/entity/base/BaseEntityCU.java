package org.example.mainservice.entity.base;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.time.ZonedDateTime;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@ToString(callSuper = true)
@MappedSuperclass
public abstract class BaseEntityCU extends BaseEntity {

    @Column(name = "create_date", nullable = false, updatable = false)
    private ZonedDateTime createDate;

    @Column(name = "update_date", nullable = false)
    private ZonedDateTime updateDate;

    @PrePersist
    void prePersist() {
        this.createDate = ZonedDateTime.now();
        this.updateDate = ZonedDateTime.now();
    }

    @PreUpdate
    void preUpdate() {
        this.updateDate = ZonedDateTime.now();
    }
}
