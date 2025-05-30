package com.teste.PowerCrm.service;

import com.teste.PowerCrm.entity.Brand;
import com.teste.PowerCrm.entity.Model;
import com.teste.PowerCrm.repository.BrandRepository;
import com.teste.PowerCrm.repository.ModelRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.Map;


@Service
public class MarcaModeloService {

    @Value("${fipe.carros.urlbase}")
    private String urlBase;

    private final BrandRepository brandRepository;
    private final ModelRepository modelRepository;
    private final RestTemplate restTemplate;

    public MarcaModeloService(BrandRepository brandRepository, ModelRepository modelRepository, RestTemplateBuilder builder) {
        this.brandRepository = brandRepository;
        this.modelRepository = modelRepository;
        this.restTemplate = builder.build();
    }

    public void sincronizarModeloEMarca() {

        if (possuiDadosDeMarcaEModelo()) {
            return;
        }

        List<Map<String, String>> brands = fetchBrandsFromFipe();

        for (Map<String, String> brandMap : brands) {
            Brand brand = salvarOuAtualizarMarca(brandMap);
            sincronizarModeloComMarcaParaSalvarNaBase(brand);
        }
    }

    private List<Map<String, String>> fetchBrandsFromFipe() {
        return Arrays.asList(restTemplate.getForObject(urlBase, Map[].class));
    }

    private boolean possuiDadosDeMarcaEModelo() {
        boolean temMarcas = brandRepository.count() > 0;
        boolean temModelos = modelRepository.count() > 0;

        return temMarcas && temModelos;
    }

    private Brand salvarOuAtualizarMarca(Map<String, String> brandMap) {
        Long brandId = Long.parseLong(brandMap.get("codigo"));
        String brandName = brandMap.get("nome");

        return brandRepository.findById(brandId).orElseGet(() -> {
            Brand newBrand = new Brand();
            newBrand.setId(brandId);
            newBrand.setName(brandName);
            return brandRepository.save(newBrand);
        });
    }

    @SuppressWarnings("unchecked")
    private void sincronizarModeloComMarcaParaSalvarNaBase(Brand brand) {
        String modelsUrl = urlBase + brand.getId() + "/modelos";

        Map<String, Object> response = restTemplate.getForObject(modelsUrl, Map.class);

        List<Map<String, Object>> modelos = (List<Map<String, Object>>) response.get("modelos");

        for (Map<String, Object> modelMap : modelos) {
            Long modelId = ((Number) modelMap.get("codigo")).longValue();
            String modelName = (String) modelMap.get("nome");

            if (!modelRepository.existsById(modelId)) {
                Model model = new Model();
                model.setId(modelId);
                model.setName(modelName);
                model.setBrand(brand);
                modelRepository.save(model);
            }
        }
    }

}
