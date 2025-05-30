package com.teste.PowerCrm.mapper;

import com.teste.PowerCrm.dto.UserDTO;
import com.teste.PowerCrm.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = VehicleMapper.class)
public interface UserMapper {

    User toEntity(UserDTO dto);
    UserDTO toDTO(User entity);
}
