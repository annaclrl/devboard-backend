package com.devboard.controller;

import com.devboard.dto.usuario.UsuarioRequestDTO;
import com.devboard.dto.usuario.UsuarioResponseDTO;
import com.devboard.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @PostMapping
    public ResponseEntity<UsuarioResponseDTO> salvarUsuario(@RequestBody @Valid UsuarioRequestDTO dto, UriComponentsBuilder uriBuilder) {
        var usuarioSalvo = usuarioService.salvarUsuario(dto);
        var uri = uriBuilder.path("/usuarios/{id}").buildAndExpand(usuarioSalvo.getId()).toUri();
        return ResponseEntity.created(uri).body(usuarioSalvo);
    }

}
