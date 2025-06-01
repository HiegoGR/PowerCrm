package com.teste.PowerCrm.service;


import com.teste.PowerCrm.dto.VehicleDTO;
import com.teste.PowerCrm.entity.Brand;
import com.teste.PowerCrm.entity.Model;
import com.teste.PowerCrm.entity.User;
import com.teste.PowerCrm.entity.Vehicle;
import com.teste.PowerCrm.mapper.VehicleMapper;
import com.teste.PowerCrm.repository.BrandRepository;
import com.teste.PowerCrm.repository.ModelRepository;
import com.teste.PowerCrm.repository.UserRepository;
import com.teste.PowerCrm.repository.VehicleRepository;
import com.teste.PowerCrm.service.event.FipeProducer;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class VehicleServiceTest {

    @InjectMocks
    private VehicleService vehicleService;

    @Mock
    private VehicleRepository vehicleRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private BrandRepository brandRepository;

    @Mock
    private ModelRepository modelRepository;

    @Mock
    private FipeService fipeService;

    @Mock
    private FipeProducer fipeProducer;

    @Mock
    private VehicleMapper vehicleMapper;

    @BeforeEach
    public void setUp() {

        vehicleService = new VehicleService(vehicleRepository, userRepository, vehicleMapper, brandRepository,
                modelRepository, fipeProducer, fipeService);
    }


    @Test
    public void salvarVeiculoComExcecaoPlacaJaCadastrada() {
        VehicleDTO dto = new VehicleDTO("Placa_Teste", 1000.00, 2020, LocalDateTime.now(),
                                    1L, 1L, 1L, 180000.00);
        Vehicle entity = new Vehicle();
        entity.setPlate("Placa_Teste");

        when(vehicleRepository.existsByPlate("Placa_Teste")).thenReturn(true);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            vehicleService.salvar(dto);
        });

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("Placa já cadastrada.", exception.getReason());
    }

    @Test
    public void salvarVeiculoComExcecaoUsuarioNaoEncontrado() {
        VehicleDTO dto = new VehicleDTO("Placa_Teste", 1000.00, 2020, LocalDateTime.now(),
                99L, 1L, 1L, 180000.00);

        when(vehicleRepository.existsByPlate(dto.getPlate())).thenReturn(false);
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            vehicleService.salvar(dto);
        });

        assertEquals("Usuário com ID 99 não encontrado", exception.getMessage());
    }

    @Test
    public void salvarVeiculoComExcecaoMarcaNaoEncontrada() {
        VehicleDTO dto = new VehicleDTO("Placa_Teste", 1000.00, 2020, LocalDateTime.now(),
                                    1L, 2010L, 1L, 180000.00);

        when(vehicleRepository.existsByPlate(dto.getPlate())).thenReturn(false);
        when(userRepository.findById(1L)).thenReturn(Optional.of(new User()));
        when(brandRepository.findById(2010L)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            vehicleService.salvar(dto);
        });

        assertEquals("Marca com ID 2010 não encontrada", exception.getMessage());
    }

    @Test
    public void salvarVeiculoComExcecaoModeloNaoEncontrado() {
        VehicleDTO dto = new VehicleDTO("Placa_Teste", 1000.00, 2020, LocalDateTime.now(),
                                    1L, 1L, 2599L, 180000.00);

        when(vehicleRepository.existsByPlate(dto.getPlate())).thenReturn(false);
        when(userRepository.findById(1L)).thenReturn(Optional.of(new User()));
        when(brandRepository.findById(1L)).thenReturn(Optional.of(new Brand()));
        when(modelRepository.findById(2599L)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            vehicleService.salvar(dto);
        });

        assertEquals("Modelo com ID 2599 não encontrado", exception.getMessage());
    }

    @Test
    public void salvarVeiculoComExcecaoModeloNaoPertenceAMarca() {
        VehicleDTO dto = new VehicleDTO("Placa_Teste", 1000.00, 2020, LocalDateTime.now(),
                                    1L, 1L, 1L, 180000.00);

        Brand marca = new Brand(1L, "Acura");
        Brand outraMarca = new Brand(2L, "Fiat");
        Model modelo = new Model(1L, "MT03", outraMarca);

        when(vehicleRepository.existsByPlate(dto.getPlate())).thenReturn(false);
        when(userRepository.findById(1L)).thenReturn(Optional.of(new User()));
        when(brandRepository.findById(1L)).thenReturn(Optional.of(marca));
        when(modelRepository.findById(1L)).thenReturn(Optional.of(modelo));

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            vehicleService.salvar(dto);
        });

        assertEquals("O modelo informado não pertence à marca selecionada", exception.getReason());
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
    }

    @Test
    public void salvarVeiculoComExcecaoAnoNaoValidoNaFipe() {
        VehicleDTO dto = new VehicleDTO("Placa_Teste", 1000.00, 2020, LocalDateTime.now(),
                                    1L, 1L, 1L, 180000.00);

        Brand marca = new Brand(1L, "Fiat");
        Model modelo = new Model(1L, "X6", marca);

        when(vehicleRepository.existsByPlate(dto.getPlate())).thenReturn(false);
        when(userRepository.findById(1L)).thenReturn(Optional.of(new User()));
        when(brandRepository.findById(1L)).thenReturn(Optional.of(marca));
        when(modelRepository.findById(1L)).thenReturn(Optional.of(modelo));
        when(fipeService.buscarCodigoAnoModeloNaFipe(1L, 1L, 2020)).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            vehicleService.salvar(dto);
        });

        assertEquals("Ano do modelo não é válido na FIPE", exception.getReason());
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
    }

}
