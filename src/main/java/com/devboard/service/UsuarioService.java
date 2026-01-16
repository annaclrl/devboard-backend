package com.devboard.service;

import com.devboard.dto.usuario.UsuarioRequestDTO;
import com.devboard.dto.usuario.UsuarioResponseDTO;
import com.devboard.mapper.UsuarioMapper;
import com.devboard.repository.UsuarioRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Transactional
    public UsuarioResponseDTO salvarUsuario(UsuarioRequestDTO dto) {
        var usuarioSalvo = usuarioRepository.save(UsuarioMapper.toEntity(dto));
        return UsuarioMapper.toResponseDTO(usuarioSalvo);
    }

    public List<UsuarioResponseDTO> listarTodosUsuarios() {
        return usuarioRepository.findAll()
                .stream()
                .map(UsuarioMapper::toResponseDTO)
                .toList();
    }
}
