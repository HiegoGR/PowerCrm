package com.teste.PowerCrm.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.teste.PowerCrm.dto.VehicleDTO;
import com.teste.PowerCrm.entity.Brand;
import com.teste.PowerCrm.entity.Model;
import com.teste.PowerCrm.entity.User;
import com.teste.PowerCrm.repository.BrandRepository;
import com.teste.PowerCrm.repository.ModelRepository;
import com.teste.PowerCrm.repository.UserRepository;
import com.teste.PowerCrm.service.FipeService;
import com.teste.PowerCrm.service.VehicleService;
import com.teste.PowerCrm.service.event.FipeProducer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
public class VehicleControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BrandRepository brandRepository;

    @Autowired
    private ModelRepository modelRepository;

    @Autowired
    private VehicleService vehicleService;


    @MockBean
    private FipeService fipeService;

    @MockBean
    private FipeProducer fipeProducer;

    private Long userId;
    private Long brandId;
    private Long modelId;

    @BeforeEach
    public void setup() {

        Brand brand = new Brand(1L,"Acura");
        brandRepository.save(brand);

        Model model = new Model(1L, "Acura Teste", brand);
        modelRepository.save(model);

        User user = new User("Teste", "teste@gmail.com","319999-9999",
                "12345678901","","Rua Teste","25A",
                "", LocalDateTime.now(), Boolean.TRUE);
        userRepository.save(user);

        userId = user.getId();
        brandId = brand.getId();
        modelId = model.getId();

        when(fipeService.buscarCodigoAnoModeloNaFipe(brandId, modelId, 2020))
                .thenReturn(Optional.of("2020-1"));

    }

    @Test
    public void salvarVeiculoComSucesso() throws Exception {
        VehicleDTO dto = new VehicleDTO("IGT-IW5F", 180000.00, 2020, LocalDateTime.now(),
                userId, brandId, modelId, 59000.00);

        mockMvc.perform(post("/api/vehicle")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.plate").value("IGT-IW5F"));
    }

    @Test
    public void buscarPorId() throws Exception {
        VehicleDTO dto = new VehicleDTO("IGT-1W5F", 180000.00, 2020, LocalDateTime.now(),
                userId, brandId, modelId, 59000.00);

        dto = vehicleService.salvar(dto);

        mockMvc.perform(get("/api/vehicle/" + dto.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.plate").value(dto.getPlate()));
    }


    @Test
    public void deletarVeiculo() throws Exception {
        VehicleDTO dto = new VehicleDTO("IGT-1W5F", 180000.00, 2020, LocalDateTime.now(),
                userId, brandId, modelId, 59000.00);

        Long idVhicle = vehicleService.salvar(dto).getId();

        mockMvc.perform(delete("/api/vehicle/" + idVhicle))
                .andExpect(status().isNoContent());
    }

}
