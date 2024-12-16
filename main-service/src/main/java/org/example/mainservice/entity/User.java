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
@Table(name = "users")
public class User extends BaseEntityCU {

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "second_name")
    private String secondName;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "telegram_username", nullable = false)
    private String telegramUsername;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "verified", nullable = false)
    private Boolean verified;

    @ManyToOne
    @JoinColumn(name = "residence_id")
    private Residence residence;
}
