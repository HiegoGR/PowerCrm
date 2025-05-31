package com.teste.PowerCrm.service;

import com.teste.PowerCrm.dto.BrandDTO;
import com.teste.PowerCrm.dto.BrandWithModelsDTO;
import com.teste.PowerCrm.dto.ModelPlaneDTO;
import com.teste.PowerCrm.entity.Brand;
import com.teste.PowerCrm.mapper.BrandMapper;
import com.teste.PowerCrm.repository.BrandRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BrandService extends CrudPadraoService<Brand, BrandDTO>{

    private final BrandRepository brandRepository;

    private final ModelService modelService;

    protected BrandService(BrandRepository brandRepository, BrandMapper brandMapper, ModelService modelService) {
        super(brandRepository, brandMapper::toEntity, brandMapper::toDTO);
        this.brandRepository = brandRepository;
        this.modelService = modelService;
    }


    public BrandWithModelsDTO buscarMarcaComModelos(Long brandId) {
        Brand brand = brandRepository.findById(brandId)
                .orElseThrow(() -> new EntityNotFoundException("Marca não encontrada com ID " + brandId));

        List<ModelPlaneDTO> models = modelService.findByBrandId(brandId).stream()
                .map(model -> new ModelPlaneDTO(model.getId(), model.getName()))
                .toList();

        return new BrandWithModelsDTO(brand.getId(), brand.getName(), models);
    }


}
