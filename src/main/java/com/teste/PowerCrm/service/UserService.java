package com.teste.PowerCrm.service;

import com.teste.PowerCrm.dto.UserDTO;
import com.teste.PowerCrm.entity.User;
import com.teste.PowerCrm.mapper.UserMapper;
import com.teste.PowerCrm.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class UserService extends CrudPadraoService<User, UserDTO>{

    private final UserRepository userRepository;

    protected UserService(UserRepository userRepository, UserMapper userMapper) {
        super(userRepository, userMapper::toEntity, userMapper::toDTO);
        this.userRepository = userRepository;
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
    public UserDTO salvar(UserDTO dto) {
        jaExisteDadosCadastrados(dto);
        return super.salvar(dto);
    }

    public void jaExisteDadosCadastrados(UserDTO dto){

        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "E-mail já cadastrado.");
        }

        if (userRepository.existsByCpf(dto.getCpf())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "CPF já cadastrado.");
        }

    }
}
