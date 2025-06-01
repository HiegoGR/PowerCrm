package com.teste.PowerCrm.service;


import com.teste.PowerCrm.dto.FipeRequestDTO;
import com.teste.PowerCrm.dto.VehicleDTO;
import com.teste.PowerCrm.entity.Brand;
import com.teste.PowerCrm.entity.Model;
import com.teste.PowerCrm.entity.User;
import com.teste.PowerCrm.entity.Vehicle;
import com.teste.PowerCrm.repository.BrandRepository;
import com.teste.PowerCrm.repository.ModelRepository;
import com.teste.PowerCrm.repository.UserRepository;
import com.teste.PowerCrm.repository.VehicleRepository;
import com.teste.PowerCrm.service.event.FipeProducer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class VehicleServiceIT {

    @Autowired
    private VehicleService vehicleService;

    @Autowired
    private VehicleRepository vehicleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BrandRepository brandRepository;

    @Autowired
    private ModelRepository modelRepository;

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
    public void salvarVeiculoComSucesso() {
        VehicleDTO vehicleDTO = new VehicleDTO("Placa_Teste",1000.00, 2020, LocalDateTime.now(),
                userId, brandId, modelId, 180000.00);

        VehicleDTO saved = vehicleService.salvar(vehicleDTO);

        assertThat(saved).isNotNull();
        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getPlate()).isEqualTo("Placa_Teste");

        verify(fipeProducer).send(any(FipeRequestDTO.class));
    }

    @Test
    public void salvarVeiculoExcecaoValidarAnoNaFipe() {
        VehicleDTO vehicleDTO = new VehicleDTO("Placa_Teste",1000.00, 20100, LocalDateTime.now(),
                userId, brandId, modelId, 180000.00);

        when(fipeService.buscarCodigoAnoModeloNaFipe(brandId, modelId, 2100))
                .thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            vehicleService.salvar(vehicleDTO);
        });

        assertThat(exception.getStatusCode().value()).isEqualTo(400);
        assertThat(exception.getReason()).isEqualTo("Ano do modelo não é válido na FIPE");
    }

    @Test
    public void atualizarVeiculoComSucesso() {
        VehicleDTO vehicleDTO = new VehicleDTO("Placa_Teste",1000.00, 2020, LocalDateTime.now(),
                userId, brandId, modelId, 180000.00);

        VehicleDTO saved = vehicleService.salvar(vehicleDTO);

        VehicleDTO updated = new VehicleDTO("Placa_Atualizada", 2000.00, 2020,
                LocalDateTime.now(), userId, brandId, modelId, 35000.00);

        VehicleDTO retorno = vehicleService.atualizar(saved.getId(), updated);

        assertThat(retorno).isNotNull();
        assertThat(retorno.getId()).isEqualTo(saved.getId());
        assertThat(retorno.getPlate()).isEqualTo("Placa_Atualizada");
        assertThat(retorno.getAdvertisedPrice()).isEqualTo(2000.00);

        Optional<Vehicle> veiculoSalvoBanco = vehicleRepository.findById(saved.getId());
        assertThat(veiculoSalvoBanco).isPresent();
        assertThat(retorno.getPlate()).isEqualTo("Placa_Atualizada");
        assertThat(retorno.getAdvertisedPrice()).isEqualTo(2000.00);
    }

    @Test
    public void listarTodosVeiculosComSucesso() {
        VehicleDTO vehicleDTO = new VehicleDTO("Placa_Teste",1000.00, 2020, LocalDateTime.now(),
                userId, brandId, modelId, 180000.00);

        VehicleDTO vehicleDTO2 = new VehicleDTO("OXP-5AF0",2000.00, 2020, LocalDateTime.now(),
                userId, brandId, modelId, 280000.00);

        vehicleService.salvar(vehicleDTO);
        vehicleService.salvar(vehicleDTO2);

        List<VehicleDTO> lista = vehicleService.listarTodos();

        assertThat(lista).isNotNull();
        assertThat(lista.size()).isGreaterThanOrEqualTo(2);

        List<String> placas = lista.stream().map(VehicleDTO::getPlate).toList();
        assertThat(placas).contains("Placa_Teste", "OXP-5AF0");
    }


}
