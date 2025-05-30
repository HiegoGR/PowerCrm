package com.teste.PowerCrm.service;

import com.teste.PowerCrm.dto.ModelDTO;
import com.teste.PowerCrm.entity.Model;
import com.teste.PowerCrm.mapper.ModelMapper;
import com.teste.PowerCrm.repository.ModelRepository;
import org.springframework.stereotype.Service;

@Service
public class ModelService extends CrudPadraoService<Model, ModelDTO>{

    protected ModelService(ModelRepository modelRepository, ModelMapper modelMapper) {
        super(modelRepository, modelMapper::toEntity, modelMapper::toDTO);
    }

}
