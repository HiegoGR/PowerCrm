package com.teste.PowerCrm.service;

import com.teste.PowerCrm.dto.UserDTO;
import com.teste.PowerCrm.entity.User;
import com.teste.PowerCrm.mapper.UserMapper;
import com.teste.PowerCrm.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserService userService;

    private UserDTO userDTO;

    private User userAtivo;
    private User userInativo;

    @BeforeEach
    public void setUp() {

        userDTO = new UserDTO("Athos Silva","athos@gmail.com",
                "31988889999", "12345678901","Avenida Vilarinho","104", "",
                "31615-250", LocalDateTime.now(), Boolean.TRUE);

        userInativo = new User("Amintas Pereira","amintas@gmail.com",
                "31988989998", "12345678901","", "Rua Pernanmbuco","100","",
                 LocalDateTime.now(), Boolean.FALSE);

        userAtivo = new User("Athos Silva","athos@gmail.com",
                "31988889999", "12345678901","Avenida Vilarinho","104", "",
                "31615-250", LocalDateTime.now(), Boolean.TRUE);

    }

    @Test
    public void salvarUsuarioComExcecaoCpfJaCadastrado() {
        when(userRepository.existsByCpf(userDTO.getCpf())).thenReturn(true);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            userService.salvar(userDTO);
        });

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("CPF já cadastrado.", exception.getReason());
        verify(userRepository, never()).save(any());
    }

    @Test
    public void salvarUsuarioComExcecaoEmailJaCadastrado() {
        when(userRepository.existsByEmail(userDTO.getEmail())).thenReturn(true);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            userService.salvar(userDTO);
        });

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("E-mail já cadastrado.", exception.getReason());
        verify(userRepository, never()).save(any());
    }

    @Test
    public void listarUsuarioPorStatusNull() {
        when(userMapper.toDTO(any(User.class))).thenReturn(userDTO);

        List<User> todos = List.of(userAtivo, userInativo);
        when(userRepository.findAll()).thenReturn(todos);

        List<UserDTO> resultado = userService.listarUsuarioPorStatus(null);

        assertThat(resultado).hasSize(2);
        verify(userRepository).findAll();
        verify(userRepository, never()).findByStatus(anyBoolean());
    }

    @Test
    public void istarUsuarioPorStatusTrue() {
        when(userMapper.toDTO(any(User.class))).thenReturn(userDTO);

        List<User> ativos = List.of(userAtivo);
        when(userRepository.findByStatus(true)).thenReturn(ativos);

        List<UserDTO> resultado = userService.listarUsuarioPorStatus(true);

        assertThat(resultado).hasSize(1);
        verify(userRepository).findByStatus(true);
        verify(userRepository, never()).findAll();
    }

    @Test
    public void istarUsuarioPorStatusFalse() {
        when(userMapper.toDTO(any(User.class))).thenReturn(userDTO);

        List<User> inativos = List.of(userInativo);
        when(userRepository.findByStatus(false)).thenReturn(inativos);

        List<UserDTO> resultado = userService.listarUsuarioPorStatus(false);

        assertThat(resultado).hasSize(1);
        verify(userRepository).findByStatus(false);
        verify(userRepository, never()).findAll();
    }

}
