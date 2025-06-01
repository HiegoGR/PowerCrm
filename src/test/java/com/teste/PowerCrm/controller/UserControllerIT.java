package com.teste.PowerCrm.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.teste.PowerCrm.dto.UserDTO;
import com.teste.PowerCrm.entity.User;
import com.teste.PowerCrm.mapper.UserMapper;
import com.teste.PowerCrm.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
public class UserControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserMapper userMapper;

    private UserDTO userDTO;

    private User usuarioSalvoBase;

    private UserDTO userRetornoSave;

    @BeforeEach
    public void setup() {

        userDTO = new UserDTO("Athos Silva","athos@gmail.com","31988889999",
                "12345678901","Avenida Vilarinho","104", "",
                "31615-250", LocalDateTime.now(), Boolean.TRUE);

    }

    @Test
    public void salvarComSucesso() throws Exception {
        mockMvc.perform(post("/api/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.email").value(userDTO.getEmail()));
    }

    @Test
    public void listar() throws Exception {
        usuarioSalvoBase = userRepository.save(userMapper.toEntity(userDTO));
        userRetornoSave = userMapper.toDTO(usuarioSalvoBase);

        mockMvc.perform(get("/api/user"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.[*].id").value(hasItem(userRetornoSave.getId().intValue())))
                .andExpect(jsonPath("$.[*].email").value(hasItem(userRetornoSave.getEmail())));
    }

    @Test
    public void deletarUsuario() throws Exception {
        usuarioSalvoBase = userRepository.save(userMapper.toEntity(userDTO));
        userRetornoSave = userMapper.toDTO(usuarioSalvoBase);

        mockMvc.perform(delete("/api/user/" + userRetornoSave.getId()))
                .andExpect(status().isNoContent());
    }


}
