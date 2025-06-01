package com.teste.PowerCrm.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "user_entity")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    private String phone;

    @Column(nullable = false, unique = true)
    private String cpf;

    private String zipCode;

    private String address;

    private String number;

    private String complement;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    private Boolean status;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Vehicle> vehicles;

    public User(String name, String email, String phone,
                String cpf, String zipCode, String address, String number,
                String complement, LocalDateTime createdAt, Boolean status) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.cpf = cpf;
        this.zipCode = zipCode;
        this.address = address;
        this.number = number;
        this.complement = complement;
        this.createdAt = createdAt;
        this.status = status;
    }
}
