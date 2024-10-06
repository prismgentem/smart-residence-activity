package org.example.smartresidenceactivity.entity.base;

import jakarta.persistence.Column;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.GeneratedValue;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.hibernate.Hibernate;

import java.util.Objects;
import java.util.UUID;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@ToString
@MappedSuperclass
public abstract class BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;

    @Override
    public final boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (isNull(o) || Hibernate.getClass(this) != Hibernate.getClass(o)) {
            return false;
        }
        var that = (BaseEntity) Hibernate.getClass(this).cast(o);

        return nonNull(getId()) && getId().equals(that.getId());
    }

    @Override
    public final int hashCode() {
        return isNull(getId())
                ? Hibernate.getClass(this).hashCode()
                : Objects.hashCode(getId());
    }
}
