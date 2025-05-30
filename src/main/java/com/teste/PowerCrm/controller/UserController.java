package com.teste.PowerCrm.controller;

import com.teste.PowerCrm.dto.UserDTO;
import com.teste.PowerCrm.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping
    public ResponseEntity<UserDTO> salvar(@RequestBody @Valid UserDTO dto) {
        UserDTO saved = userService.salvar(dto);
        URI location = URI.create("/api/user/" + saved.getId());
        return ResponseEntity.created(location).body(saved);
    }

    @GetMapping
    public ResponseEntity<List<UserDTO>> listar() {
        return ResponseEntity.ok(userService.listarTodos());
    }

    @GetMapping("/{idUser}")
    public ResponseEntity<UserDTO> buscarPorId(@PathVariable Long idUser) {
        return userService.buscarPorId(idUser)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{idUser}")
    public ResponseEntity<UserDTO> atualizarUsuario(@PathVariable Long idUser, @Valid @RequestBody UserDTO dto){
        UserDTO newDto = userService.atualizar(idUser,dto);
        return ResponseEntity.ok().body(newDto);
    }

    @GetMapping("/periodo")
    public ResponseEntity<List<UserDTO>> buscarPorPeriodo(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fim
    ) {
        return ResponseEntity.ok(userService.buscarPorPeriodo(inicio, fim));
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarUsuario(@PathVariable Long id){
        userService.deletar(id);
        return ResponseEntity.noContent().build();
    }

}
