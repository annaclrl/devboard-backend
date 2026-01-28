package com.devboard.mapper;

import com.devboard.dto.perfil.PerfilRequestDTO;
import com.devboard.dto.perfil.PerfilResponseDTO;
import com.devboard.model.Perfil;

public class PerfilMapper {

    public static Perfil toEntity(PerfilRequestDTO dto) {
        if (dto == null) return null;

        Perfil perfil = new Perfil();
        perfil.setDescricao(dto.getDescricao());

        return perfil;
    }

    public static PerfilResponseDTO toResponseDTO(Perfil perfil) {
        if (perfil == null) return null;

        return new PerfilResponseDTO(
                perfil.getId(),
                perfil.getDescricao()
        );
    }
}
