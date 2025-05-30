package com.teste.PowerCrm.mapper;

import com.teste.PowerCrm.dto.ModelDTO;
import com.teste.PowerCrm.entity.Model;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ModelMapper {

    Model toEntity(ModelDTO dto);
    ModelDTO toDTO(Model entity);
}
