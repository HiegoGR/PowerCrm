package com.teste.PowerCrm.mapper;

import com.teste.PowerCrm.dto.BrandDTO;
import com.teste.PowerCrm.entity.Brand;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BrandMapper {

    Brand toEntity(BrandDTO dto);
    BrandDTO toDTO(Brand entity);
}
