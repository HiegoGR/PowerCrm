package com.teste.PowerCrm.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "brand")
public class Brand {

    @Id
    private Long id;

    private String name;

}
