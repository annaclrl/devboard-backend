package com.devboard.dto.perfil;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonPropertyOrder({"id_perfil", "descricao"})
public class PerfilResponseDTO {

    @JsonProperty("id_perfil")
    private Long id;
    private String descricao;
}
