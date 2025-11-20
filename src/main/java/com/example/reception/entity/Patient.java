package com.example.reception.entity;

import jakarta.persistence.*;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.*;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "patients")
@SQLDelete(sql = "UPDATE patients SET deleted = true WHERE id=?")
@FilterDef(name = "deletedPatientFilter",
        parameters = @ParamDef(name = "isDeleted", type = org.hibernate.type.descriptor.java.BooleanJavaType.class))
@Filter(name = "deletedPatientFilter", condition = "deleted = :isDeleted")
public class Patient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "passport", nullable = false)
    private String passport;

    @Column(name = "phone")
    private String phone;

    @Column(name = "birth_date", nullable = false)
    private LocalDate birthDate;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "deleted")
    private Boolean deleted = Boolean.FALSE;
}