package com.devboard.controller;

import com.devboard.dto.perfil.PerfilRequestDTO;
import com.devboard.dto.perfil.PerfilResponseDTO;
import com.devboard.service.PerfilService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@RestController
@RequestMapping("/perfis")
public class PerfilController {

    @Autowired
    private PerfilService perfilService;

    @PostMapping
    public ResponseEntity<PerfilResponseDTO> salvarPerfil(@RequestBody @Valid PerfilRequestDTO dto, UriComponentsBuilder uriBuilder) {
        var perfilSalvo = perfilService.salvarPerfil(dto);
        var uri = uriBuilder.path("/perfis/{id}").buildAndExpand(perfilSalvo.getId()).toUri();
        return ResponseEntity.created(uri).body(perfilSalvo);
    }

    @GetMapping
    public ResponseEntity<List<PerfilResponseDTO>> listarTodosPerfis() {
        var perfis = perfilService.listarTodosPerfis();
        return ResponseEntity.ok(perfis);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PerfilResponseDTO> buscarPerfilPorId(@PathVariable Long id) {
        var perfil = perfilService.buscarPerfilPorId(id);
        return ResponseEntity.ok(perfil);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PerfilResponseDTO> atualizarPerfil(@PathVariable Long id, @RequestBody @Valid PerfilRequestDTO dto) {
        var perfilAtualizado = perfilService.atualizarPerfil(id, dto);
        return ResponseEntity.ok(perfilAtualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarPerfil(@PathVariable Long id){
        perfilService.deletarPerfil(id);
        return ResponseEntity.noContent().build();
    }
}
