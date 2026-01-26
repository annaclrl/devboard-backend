package com.devboard.controller;

import com.devboard.dto.usuario.UsuarioRequestDTO;
import com.devboard.dto.usuario.UsuarioResponseDTO;
import com.devboard.exception.EntidadeNaoEncontrada;
import com.devboard.service.UsuarioService;
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
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UsuarioController.class)
@AutoConfigureMockMvc
class UsuarioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private UsuarioService usuarioService;

    @Test
    void deveSalvarUsuario() throws Exception {

        UsuarioRequestDTO usuario = new UsuarioRequestDTO("Anna", "anna@email.com", "123456");
        UsuarioResponseDTO usuarioSalvo = new UsuarioResponseDTO(1L, "Anna", "anna@email.com", "123456");
        when(usuarioService.salvarUsuario(any(UsuarioRequestDTO.class))).thenReturn(usuarioSalvo);

        mockMvc.perform(post("/usuarios")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(usuario)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id_usuario").value(1));
    }

    @Test
    void deveRetornarBadRequestQuandoDadosInvalidos() throws Exception {
        UsuarioRequestDTO usuarioInvalido = new UsuarioRequestDTO("", null, "123456");

        mockMvc.perform(post("/usuarios")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(usuarioInvalido)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message").value("Erro de validação"))
                .andExpect(jsonPath("$.errors.nome").exists())
                .andExpect(jsonPath("$.errors.email").exists());
    }

    @Test
    void deveListarTodosUsuarios() throws Exception {

        // arrange
        List<UsuarioResponseDTO> usuarios = List.of(
                new UsuarioResponseDTO(1L, "Felipe", "felipe@email.com", "123456"),
                new UsuarioResponseDTO(2L, "Anna", "anna@email.com", "123456")
        );

        when(usuarioService.listarTodosUsuarios()).thenReturn(usuarios);

        // act & assert
        mockMvc.perform(get("/usuarios")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id_usuario").value(1))
                .andExpect(jsonPath("$[0].nome").value("Felipe"))
                .andExpect(jsonPath("$[1].id_usuario").value(2))
                .andExpect(jsonPath("$[1].nome").value("Anna"));
    }

    @Test
    void deveBuscarUsuarioPorIdComSucesso() throws Exception {

        // arrange
        Long id = 1L;

        UsuarioResponseDTO usuario = new UsuarioResponseDTO(
                id,
                "Felipe",
                "felipe@email.com",
                "123456"
        );

        when(usuarioService.buscarUsuarioPorId(id)).thenReturn(usuario);

        // act & assert
        mockMvc.perform(get("/usuarios/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id_usuario").value(1))
                .andExpect(jsonPath("$.nome").value("Felipe"))
                .andExpect(jsonPath("$.email").value("felipe@email.com"));
    }

    @Test
    void deveRetornarNotFoundQuandoUsuarioNaoExistir() throws Exception {

        // arrange
        Long id = 99L;

        when(usuarioService.buscarUsuarioPorId(id))
                .thenThrow(new EntidadeNaoEncontrada(
                        "Usuário com id 99 não encontrado!"
                ));

        // act & assert
        mockMvc.perform(get("/usuarios/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message")
                        .value("Usuário com id 99 não encontrado!"));
    }

    @Test
    void deveAtualizarUsuarioComSucesso() throws Exception {

        // arrange
        Long id = 1L;

        UsuarioRequestDTO dto = new UsuarioRequestDTO(
                "Felipe Atualizado",
                "felipe.novo@email.com",
                "654321"
        );

        UsuarioResponseDTO usuarioAtualizado = new UsuarioResponseDTO(
                id,
                "Felipe Atualizado",
                "felipe.novo@email.com",
                "654321"
        );

        when(usuarioService.atualizarUsuario(any(Long.class), any(UsuarioRequestDTO.class)))
                .thenReturn(usuarioAtualizado);

        // act & assert
        mockMvc.perform(put("/usuarios/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id_usuario").value(1))
                .andExpect(jsonPath("$.nome").value("Felipe Atualizado"))
                .andExpect(jsonPath("$.email").value("felipe.novo@email.com"));
    }

    @Test
    void deveRetornarBadRequestAoAtualizarUsuarioComDadosInvalidos() throws Exception {

        // arrange
        UsuarioRequestDTO dtoInvalido = new UsuarioRequestDTO(
                "",
                null,
                "123456"
        );

        // act & assert
        mockMvc.perform(put("/usuarios/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dtoInvalido)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message").value("Erro de validação"))
                .andExpect(jsonPath("$.errors.nome").exists())
                .andExpect(jsonPath("$.errors.email").exists());
    }

    @Test
    void deveRetornarNotFoundAoAtualizarUsuarioInexistente() throws Exception {

        // arrange
        Long id = 99L;

        UsuarioRequestDTO dto = new UsuarioRequestDTO(
                "Nome",
                "email@email.com",
                "123456"
        );

        when(usuarioService.atualizarUsuario(any(Long.class), any(UsuarioRequestDTO.class)))
                .thenThrow(new EntidadeNaoEncontrada(
                        "Usuário com id 99 não encontrado!"
                ));

        // act & assert
        mockMvc.perform(put("/usuarios/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message")
                        .value("Usuário com id 99 não encontrado!"));
    }
}