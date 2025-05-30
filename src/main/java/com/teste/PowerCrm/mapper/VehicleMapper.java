package com.teste.PowerCrm.mapper;

import com.teste.PowerCrm.dto.VehicleDTO;
import com.teste.PowerCrm.entity.Vehicle;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface VehicleMapper {

    @Mapping(target = "user", ignore = true)
    @Mapping(target = "brand", ignore = true)
    @Mapping(target = "model", ignore = true)
    Vehicle toEntity(VehicleDTO dto);

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "brand.id", target = "brandId")
    @Mapping(source = "model.id", target = "modelId")
    VehicleDTO toDTO(Vehicle entity);
}
