package org.example.mainservice.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.example.mainservice.entity.base.BaseEntityCU;

import java.util.UUID;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@ToString(callSuper = true)
@Entity
@Table(name = "subscriber")
public class Subscriber extends BaseEntityCU {
    @Column(name = "email", nullable = false)
    private String email;
    @Column(name = "telegram_username", nullable = false)
    private String telegramUsername;
    @Column(name = "residence_id", nullable = false)
    private UUID residenceId;
}
