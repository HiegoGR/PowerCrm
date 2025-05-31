package com.teste.PowerCrm.service;

import com.teste.PowerCrm.dto.FipeRequestDTO;
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
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class VehicleService  extends CrudPadraoService<Vehicle, VehicleDTO>{

    private final VehicleRepository vehicleRepository;
    private final UserRepository userRepository;
    private final VehicleMapper vehicleMapper;
    private final BrandRepository brandRepository;
    private final ModelRepository modelRepository;
    private final FipeProducer fipeProducer;
    private final FipeService fipeService;

    public VehicleService(VehicleRepository vehicleRepository, UserRepository userRepository,
                          VehicleMapper vehicleMapper, BrandRepository brandRepository,
                          ModelRepository modelRepository, FipeProducer fipeProducer,
                          FipeService fipeService) {
        super(vehicleRepository, vehicleMapper::toEntity, vehicleMapper::toDTO);

        this.vehicleRepository = vehicleRepository;
        this.userRepository = userRepository;
        this.vehicleMapper = vehicleMapper;
        this.brandRepository = brandRepository;
        this.modelRepository = modelRepository;
        this.fipeProducer = fipeProducer;
        this.fipeService = fipeService;
    }

    @Transactional
    @CacheEvict(value = { "vehicleList", "vehicleById" }, allEntries = true)
    @Override
    public VehicleDTO salvar(VehicleDTO dto) {

        existePlacaCadastrada(dto.getPlate());
        User user = buscarUsuario(dto.getUserId());
        Brand brand = buscarMarca(dto.getBrandId());
        Model model = buscarModelo(dto.getModelId());

        validarModeloComMarca(model, brand);
        String codigoAnoModelo = validarAnoNaFipe(brand.getId(), model.getId(), dto.getYear());

        Vehicle entity = vehicleMapper.toEntity(dto);
        entity.setUser(user);
        entity.setBrand(brand);
        entity.setModel(model);
        entity.setCreatedAt(LocalDateTime.now());

        Vehicle saved = vehicleRepository.save(entity);

        fipeProducer.send(new FipeRequestDTO(saved.getId(), brand.getId(), model.getId(), saved.getYear(), codigoAnoModelo));

        return vehicleMapper.toDTO(saved);
    }

    @Override
    public VehicleDTO atualizar(Long id, VehicleDTO dto) {

        User user = buscarUsuario(dto.getUserId());
        Brand brand = buscarMarca(dto.getBrandId());
        Model model = buscarModelo(dto.getModelId());

        Vehicle entity = vehicleMapper.toEntity(dto);
        entity.setId(id);
        entity.setUser(user);
        entity.setBrand(brand);
        entity.setModel(model);

        Vehicle updated = vehicleRepository.save(entity);
        return vehicleMapper.toDTO(updated);
    }

    @Cacheable("vehicleList")
    @Override
    public List<VehicleDTO> listarTodos() {
        return super.listarTodos();
    }

    @Cacheable(value = "vehicleById", key = "#id")
    @Override
    public Optional<VehicleDTO> buscarPorId(Long id) {
        return super.buscarPorId(id);
    }

    private void existePlacaCadastrada(String plate) {
        if (vehicleRepository.existsByPlate(plate)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Placa já cadastrada.");
        }
    }

    private User buscarUsuario(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Usuário com ID " + id + " não encontrado"));
    }

    private Brand buscarMarca(Long id) {
        return brandRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Marca com ID " + id + " não encontrada"));
    }

    private Model buscarModelo(Long id) {
        return modelRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Modelo com ID " + id + " não encontrado"));
    }

    private void validarModeloComMarca(Model model, Brand brand) {
        if (!model.getBrand().getId().equals(brand.getId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "O modelo informado não pertence à marca selecionada");
        }
    }

    private String validarAnoNaFipe(Long brandId, Long modelId, int ano) {
        return fipeService.buscarCodigoAnoModeloNaFipe(brandId, modelId, ano)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Ano do modelo não é válido na FIPE"));
    }

}
