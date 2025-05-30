package com.teste.PowerCrm.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FipeRequestDTO {

    private Long vehicleId;

    private Long brandId;

    private Long modelId;

    private Integer year;

    private String anoCodigo;
}
