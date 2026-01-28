package com.devboard.service;

import com.devboard.dto.perfil.PerfilRequestDTO;
import com.devboard.dto.perfil.PerfilResponseDTO;
import com.devboard.exception.EntidadeNaoEncontrada;
import com.devboard.mapper.PerfilMapper;
import com.devboard.model.Perfil;
import com.devboard.repository.PerfilRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PerfilService {

    @Autowired
    private PerfilRepository perfilRepository;

    @Transactional
    public PerfilResponseDTO salvarPerfil(PerfilRequestDTO dto) {
        var perfilSalvo = perfilRepository.save(PerfilMapper.toEntity(dto));
        return PerfilMapper.toResponseDTO(perfilSalvo);
    }

    public List<PerfilResponseDTO> listarTodosPerfis() {
        return perfilRepository.findAll()
                .stream()
                .map(PerfilMapper::toResponseDTO)
                .toList();
    }

    public PerfilResponseDTO buscarPerfilPorId(Long id) {
        var perfil = buscarEntidadePerfilPorId(id);

        return PerfilMapper.toResponseDTO(perfil);
    }

    @Transactional
    public PerfilResponseDTO atualizarPerfil(Long id, PerfilRequestDTO dto) {
        var perfilExistente = buscarEntidadePerfilPorId(id);

        perfilExistente.setDescricao(dto.getDescricao());

        return PerfilMapper.toResponseDTO(perfilRepository.save(perfilExistente));
    }

    @Transactional
    public void deletarPerfil(Long id) {
        var perfil = buscarEntidadePerfilPorId(id);
        perfilRepository.delete(perfil);
    }

    private Perfil buscarEntidadePerfilPorId(Long id) {
        return perfilRepository.findById(id)
                .orElseThrow(() -> new EntidadeNaoEncontrada("Perfil com id " + id + " não encontrado!"));
    }
}
