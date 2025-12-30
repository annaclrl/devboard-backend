package com.devboard.service;

import com.devboard.dto.usuario.UsuarioRequestDTO;
import com.devboard.dto.usuario.UsuarioResponseDTO;
import com.devboard.mapper.UsuarioMapper;
import com.devboard.repository.UsuarioRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Transactional
    public UsuarioResponseDTO salvarUsuario(UsuarioRequestDTO dto) {
        var usuarioEntity = UsuarioMapper.toEntity(dto);
        var usuarioSalvo = usuarioRepository.save(usuarioEntity);
        var usuarioResponse = UsuarioMapper.toResponseDTO(usuarioSalvo);
        return usuarioResponse;
    }

}
