package com.example.reception.entity;

import jakarta.persistence.*;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.hibernate.annotations.*;
import org.hibernate.type.descriptor.java.BooleanJavaType;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "patients")
@SQLDelete(sql = "UPDATE patients SET deleted = true WHERE id=?")
@FilterDef(name = "deletedPatientFilter", applyToLoadByKey = true,
        parameters = @ParamDef(name = "isDeleted", type = BooleanJavaType.class))
@Filter(name = "deletedPatientFilter", condition = "deleted = :isDeleted")
public class Patient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "first_name", nullable = false)
    @NotBlank
    private String firstName;

    @Column(name = "last_name", nullable = false)
    @NotBlank
    private String lastName;

    @Column(name = "passport", nullable = false)
    @NotBlank
    private String passport;

    @Column(name = "phone")
    private String phone;

    @Column(name = "birth_date", nullable = false)
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate birthDate;

    @Column(name = "email", nullable = false, unique = true)
    @Email
    @NotBlank
    private String email;

    @Column(name = "deleted")
    private boolean isDeleted = false;
}