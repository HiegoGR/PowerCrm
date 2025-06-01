package com.teste.PowerCrm.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "vehicle")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Vehicle {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false,unique = true)
    private String plate;

    @Column(nullable = false)
    private Double advertisedPrice;

    @Column(name = "vehicle_year", nullable = false)
    private Integer year;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "brand_id")
    private Brand brand;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "model_id")
    private Model model;

    private Double fipePrice;

    public Vehicle(String plate, Double advertisedPrice, Integer year, LocalDateTime createdAt,
                   User user, Brand brand, Model model, Double fipePrice) {
        this.plate = plate;
        this.advertisedPrice = advertisedPrice;
        this.year = year;
        this.createdAt = createdAt;
        this.user = user;
        this.brand = brand;
        this.model = model;
        this.fipePrice = fipePrice;
    }
}
