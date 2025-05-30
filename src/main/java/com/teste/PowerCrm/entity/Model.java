package com.teste.PowerCrm.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "model")
public class Model {

    @Id
    private Long id;

    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "brand_id", nullable = false)
    private Brand brand;

}
