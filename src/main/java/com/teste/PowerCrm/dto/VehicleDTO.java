package com.teste.PowerCrm.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VehicleDTO {

    private Long id;

    @NotBlank
    private String plate;

    @NotNull
    private Double advertisedPrice;

    @NotNull
    private Integer year;

    private LocalDateTime createdAt = LocalDateTime.now();

    @NotNull
    private Long userId;

    private Long brandId;

    private Long modelId;

    private Long fipePrice;
}
