package com.teste.PowerCrm.controller;

import com.teste.PowerCrm.dto.ModelDTO;
import com.teste.PowerCrm.service.ModelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/model")
public class ModelController {

    @Autowired
    private ModelService modelService;

    @GetMapping
    public ResponseEntity<List<ModelDTO>> listar() {
        return ResponseEntity.ok(modelService.listarTodos());
    }

    @GetMapping("/{idModel}")
    public ResponseEntity<ModelDTO> buscarPorId(@PathVariable Long idModel) {
        return modelService.buscarPorId(idModel)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
