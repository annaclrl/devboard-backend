package com.devboard.controller;

import com.devboard.dto.perfil.PerfilRequestDTO;
import com.devboard.dto.perfil.PerfilResponseDTO;
import com.devboard.exception.EntidadeNaoEncontrada;
import com.devboard.service.PerfilService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PerfilController.class)
@AutoConfigureMockMvc
class PerfilControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private PerfilService perfilService;

    @Test
    void deveSalvarPerfil() throws Exception {

        // arrange
        PerfilRequestDTO dto = new PerfilRequestDTO("ADMIN");

        PerfilResponseDTO perfilSalvo = new PerfilResponseDTO(
                1L,
                "ADMIN"
        );

        when(perfilService.salvarPerfil(any(PerfilRequestDTO.class)))
                .thenReturn(perfilSalvo);

        // act & assert
        mockMvc.perform(post("/perfis")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id_perfil").value(1))
                .andExpect(jsonPath("$.descricao").value("ADMIN"));
    }

    @Test
    void deveRetornarBadRequestQuandoDadosInvalidos() throws Exception {

        // arrange
        PerfilRequestDTO dtoInvalido = new PerfilRequestDTO("");

        // act & assert
        mockMvc.perform(post("/perfis")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dtoInvalido)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message").value("Erro de validação"))
                .andExpect(jsonPath("$.errors.descricao").exists());
    }

    @Test
    void deveListarTodosPerfis() throws Exception {

        // arrange
        List<PerfilResponseDTO> perfis = List.of(
                new PerfilResponseDTO(1L, "ADMIN"),
                new PerfilResponseDTO(2L, "USUARIO")
        );

        when(perfilService.listarTodosPerfis()).thenReturn(perfis);

        // act & assert
        mockMvc.perform(get("/perfis")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id_perfil").value(1))
                .andExpect(jsonPath("$[0].descricao").value("ADMIN"))
                .andExpect(jsonPath("$[1].id_perfil").value(2))
                .andExpect(jsonPath("$[1].descricao").value("USUARIO"));
    }

    @Test
    void deveBuscarPerfilPorIdComSucesso() throws Exception {

        // arrange
        Long id = 1L;

        PerfilResponseDTO perfil = new PerfilResponseDTO(
                id,
                "ADMIN"
        );

        when(perfilService.buscarPerfilPorId(id))
                .thenReturn(perfil);

        // act & assert
        mockMvc.perform(get("/perfis/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id_perfil").value(1))
                .andExpect(jsonPath("$.descricao").value("ADMIN"));
    }

    @Test
    void deveRetornarNotFoundQuandoPerfilNaoExistir() throws Exception {

        // arrange
        Long id = 99L;

        when(perfilService.buscarPerfilPorId(id))
                .thenThrow(new EntidadeNaoEncontrada(
                        "Perfil com id 99 não encontrado!"
                ));

        // act & assert
        mockMvc.perform(get("/perfis/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message")
                        .value("Perfil com id 99 não encontrado!"));
    }

    @Test
    void deveAtualizarPerfilComSucesso() throws Exception {

        // arrange
        Long id = 1L;

        PerfilRequestDTO dto = new PerfilRequestDTO("ADMIN");

        PerfilResponseDTO perfilAtualizado = new PerfilResponseDTO(
                id,
                "ADMIN"
        );

        when(perfilService.atualizarPerfil(any(Long.class), any(PerfilRequestDTO.class)))
                .thenReturn(perfilAtualizado);

        // act & assert
        mockMvc.perform(put("/perfis/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id_perfil").value(1))
                .andExpect(jsonPath("$.descricao").value("ADMIN"));
    }

    @Test
    void deveRetornarBadRequestAoAtualizarPerfilComDadosInvalidos() throws Exception {

        // arrange
        PerfilRequestDTO dtoInvalido = new PerfilRequestDTO("");

        // act & assert
        mockMvc.perform(put("/perfis/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dtoInvalido)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message").value("Erro de validação"))
                .andExpect(jsonPath("$.errors.descricao").exists());
    }

    @Test
    void deveRetornarNotFoundAoAtualizarPerfilInexistente() throws Exception {

        // arrange
        Long id = 99L;

        PerfilRequestDTO dto = new PerfilRequestDTO("ADMIN");

        when(perfilService.atualizarPerfil(any(Long.class), any(PerfilRequestDTO.class)))
                .thenThrow(new EntidadeNaoEncontrada(
                        "Perfil com id 99 não encontrado!"
                ));

        // act & assert
        mockMvc.perform(put("/perfis/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message")
                        .value("Perfil com id 99 não encontrado!"));
    }

    @Test
    void deveDeletarPerfilComSucesso() throws Exception {

        // arrange
        Long id = 1L;

        // act & assert
        mockMvc.perform(delete("/perfis/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    void deveRetornarNotFoundAoDeletarPerfilInexistente() throws Exception {

        // arrange
        Long id = 99L;

        doThrow(new EntidadeNaoEncontrada(
                "Perfil com id 99 não encontrado!"
        )).when(perfilService).deletarPerfil(id);

        // act & assert
        mockMvc.perform(delete("/perfis/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message")
                        .value("Perfil com id 99 não encontrado!"));
    }

}