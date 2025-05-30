package com.teste.PowerCrm.service;

import com.teste.PowerCrm.dto.UserDTO;
import com.teste.PowerCrm.entity.User;
import com.teste.PowerCrm.mapper.UserMapper;
import com.teste.PowerCrm.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class UserService extends CrudPadraoService<User, UserDTO>{

    protected UserService(UserRepository userRepository, UserMapper userMapper) {
        super(userRepository, userMapper::toEntity, userMapper::toDTO);
    }

    public List<UserDTO> buscarPorPeriodo(LocalDate inicio, LocalDate fim) {

        LocalDateTime dataInicio = inicio.atStartOfDay();
        LocalDateTime dataFim = fim.plusDays(1).atStartOfDay().minusNanos(1);

        return ((UserRepository) getRepository())
                .findAllByCreatedAtBetween(dataInicio, dataFim)
                .stream()
                .map(getEntityToDto())
                .toList();
    }

    @Override
    public UserDTO atualizar(Long id, UserDTO dto) {
        getRepository().findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Usuário com ID " + id + " não encontrado."));

        return super.atualizar(id, dto);
    }
}
