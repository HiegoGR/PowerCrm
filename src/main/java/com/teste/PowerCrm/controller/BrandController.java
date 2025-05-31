package com.teste.PowerCrm.controller;

import com.teste.PowerCrm.dto.BrandDTO;
import com.teste.PowerCrm.dto.BrandWithModelsDTO;
import com.teste.PowerCrm.service.BrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/brand")
public class BrandController {

    @Autowired
    private BrandService brandService;

    @GetMapping
    public ResponseEntity<List<BrandDTO>> listar() {
        return ResponseEntity.ok(brandService.listarTodos());
    }

    @GetMapping("/{idBrand}")
    public ResponseEntity<BrandDTO> buscarPorId(@PathVariable Long idBrand) {
        return brandService.buscarPorId(idBrand)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/by-models/{idBrand}")
    public ResponseEntity<BrandWithModelsDTO> buscarModelosPorMarca(@PathVariable Long idBrand) {
        return ResponseEntity.ok(brandService.buscarMarcaComModelos(idBrand));
    }
}
