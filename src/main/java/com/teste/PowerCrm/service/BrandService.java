package com.teste.PowerCrm.service;

import com.teste.PowerCrm.dto.BrandDTO;
import com.teste.PowerCrm.entity.Brand;
import com.teste.PowerCrm.mapper.BrandMapper;
import com.teste.PowerCrm.repository.BrandRepository;
import org.springframework.stereotype.Service;

@Service
public class BrandService extends CrudPadraoService<Brand, BrandDTO>{

    protected BrandService(BrandRepository brandRepository, BrandMapper brandMapper) {
        super(brandRepository, brandMapper::toEntity, brandMapper::toDTO);
    }

}
