package com.teste.PowerCrm.controller;

import com.teste.PowerCrm.dto.VehicleDTO;
import com.teste.PowerCrm.service.VehicleService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/vehicle")
public class VehicleController {

    @Autowired
    private VehicleService vehicleService;

    @PostMapping
    public ResponseEntity<VehicleDTO> salvar(@RequestBody @Valid VehicleDTO dto) {
        VehicleDTO saved = vehicleService.salvar(dto);
        URI location = URI.create("/api/vehicle/" + saved.getId());
        return ResponseEntity.created(location).body(saved);
    }

    @GetMapping
    public ResponseEntity<List<VehicleDTO>> listar() {
        return ResponseEntity.ok(vehicleService.listarTodos());
    }

    @GetMapping("/{idVehicle}")
    public ResponseEntity<VehicleDTO> buscarPorId(@PathVariable Long idVehicle) {
        return vehicleService.buscarPorId(idVehicle)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{idVehicle}")
    public ResponseEntity<VehicleDTO> atualizar(@PathVariable Long idVehicle, @Valid @RequestBody VehicleDTO dto){
        VehicleDTO newDto = vehicleService.atualizar(idVehicle,dto);
        return ResponseEntity.ok().body(newDto);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id){
        vehicleService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
