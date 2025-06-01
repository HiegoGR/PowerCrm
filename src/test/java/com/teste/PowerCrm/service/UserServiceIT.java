package com.teste.PowerCrm.service;

import com.teste.PowerCrm.dto.UserDTO;
import com.teste.PowerCrm.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class UserServiceIT {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    private UserDTO userDTO;

    private UserDTO usuarioSalvoBase;

    @BeforeEach
    public void setup() {
        userDTO = new UserDTO("Athos Silva","athos@gmail.com","12345678901",
                "31988889999", "Avenida Vilarinho","104", "",
                "31615-250", LocalDateTime.now(), Boolean.TRUE);

        usuarioSalvoBase = userService.salvar(userDTO);
    }

    @Test
    public void salvarUsuarioComSucesso() {

        assertThat(usuarioSalvoBase).isNotNull();
        assertThat(usuarioSalvoBase.getId()).isNotNull();
        assertThat(usuarioSalvoBase.getEmail()).isEqualTo("athos@gmail.com");
    }

    @Test
    public void salvarUsuarioComExcecao() {
        userDTO.setCpf("01987456321");

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> userService.salvar(userDTO));

        assertThat(exception.getStatusCode().value()).isEqualTo(400);
        assertThat(exception.getReason()).isEqualTo("E-mail já cadastrado.");
    }

    @Test
    public void buscarUsuariosPorPeriodoComSucesso() {
        LocalDate inicio = LocalDate.now().minusDays(1);
        LocalDate fim = LocalDate.now().plusDays(1);

        List<UserDTO> resultado = userService.buscarPorPeriodo(inicio, fim);

        assertThat(resultado).isNotEmpty();
        assertThat(resultado.get(0).getEmail()).isEqualTo("athos@gmail.com");
    }


}
