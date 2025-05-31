package com.teste.PowerCrm.service;

import com.teste.PowerCrm.dto.ModelDTO;
import com.teste.PowerCrm.entity.Model;
import com.teste.PowerCrm.mapper.ModelMapper;
import com.teste.PowerCrm.repository.ModelRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ModelService extends CrudPadraoService<Model, ModelDTO>{

    private final ModelRepository modelRepository;

    protected ModelService(ModelRepository modelRepository, ModelMapper modelMapper) {
        super(modelRepository, modelMapper::toEntity, modelMapper::toDTO);
        this.modelRepository = modelRepository;
    }

    public List<Model> findByBrandId(Long idBrand){
        return modelRepository.findByBrandId(idBrand);
    }

}
