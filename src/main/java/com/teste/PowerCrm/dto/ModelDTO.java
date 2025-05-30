package com.teste.PowerCrm.dto;

import com.teste.PowerCrm.entity.Brand;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ModelDTO {

    private Long id;

    private String name;

    private Brand brand;

}
