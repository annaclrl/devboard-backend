package com.devboard.service;

import com.devboard.dto.perfil.PerfilRequestDTO;
import com.devboard.dto.perfil.PerfilResponseDTO;
import com.devboard.exception.EntidadeNaoEncontrada;
import com.devboard.model.Perfil;
import com.devboard.repository.PerfilRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PerfilServiceTest {

    @Mock
    private PerfilRepository perfilRepository;

    @InjectMocks
    private PerfilService perfilService;

    @Test
    void deveSalvarPerfil() {
        // arrange
        PerfilRequestDTO dto = new PerfilRequestDTO("ADMIN");

        Perfil perfilSalvo = new Perfil(1L, "ADMIN", List.of());

        when(perfilRepository.save(any(Perfil.class))).thenReturn(perfilSalvo);

        // act
        PerfilResponseDTO resultado = perfilService.salvarPerfil(dto);

        // assert
        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("ADMIN", resultado.getDescricao());

        verify(perfilRepository).save(any(Perfil.class));
    }

    @Test
    void deveListarTodosPerfis() {
        // arrange
        List<Perfil> perfis = List.of(
                new Perfil(1L, "ADMIN", List.of()),
                new Perfil(2L, "USUARIO", List.of())
        );

        when(perfilRepository.findAll()).thenReturn(perfis);

        // act
        List<PerfilResponseDTO> resultado = perfilService.listarTodosPerfis();

        // assert
        assertEquals(2, resultado.size());
        assertEquals("ADMIN", resultado.get(0).getDescricao());

        verify(perfilRepository).findAll();
    }

    @Test
    void deveBuscarPerfilPorIdComSucesso() {
        // arrange
        Long id = 1L;

        Perfil perfil = new Perfil(id, "ADMIN", List.of());

        when(perfilRepository.findById(id)).thenReturn(Optional.of(perfil));

        // act
        PerfilResponseDTO resultado = perfilService.buscarPerfilPorId(id);

        // assert
        assertNotNull(resultado);
        assertEquals(id, resultado.getId());
        assertEquals("ADMIN", resultado.getDescricao());

        verify(perfilRepository).findById(id);
    }

    @Test
    void deveLancarExcecaoQuandoPerfilNaoEncontrado() {
        // arrange
        Long id = 1L;

        when(perfilRepository.findById(id)).thenReturn(Optional.empty());

        // act + assert
        EntidadeNaoEncontrada exception = assertThrows(
                EntidadeNaoEncontrada.class,
                () -> perfilService.buscarPerfilPorId(id)
        );

        assertEquals("Perfil com id 1 não encontrado!", exception.getMessage());

        verify(perfilRepository).findById(id);
    }

    @Test
    void deveAtualizarPerfilComSucesso() {
        // arrange
        Long id = 1L;

        Perfil perfilExistente = new Perfil(id, "USUARIO", List.of());

        PerfilRequestDTO dtoAtualizacao = new PerfilRequestDTO("ADMIN");

        when(perfilRepository.findById(id)).thenReturn(Optional.of(perfilExistente));

        when(perfilRepository.save(any(Perfil.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // act
        PerfilResponseDTO resultado = perfilService.atualizarPerfil(id, dtoAtualizacao);

        // assert
        assertNotNull(resultado);
        assertEquals(id, resultado.getId());
        assertEquals("ADMIN", resultado.getDescricao());

        verify(perfilRepository).findById(id);
        verify(perfilRepository).save(perfilExistente);
    }

    @Test
    void deveLancarExcecaoAoAtualizarPerfilInexistente() {
        // arrange
        Long id = 1L;

        PerfilRequestDTO dto = new PerfilRequestDTO("ADMIN");

        when(perfilRepository.findById(id)).thenReturn(Optional.empty());

        // act + assert
        EntidadeNaoEncontrada exception = assertThrows(
                EntidadeNaoEncontrada.class,
                () -> perfilService.atualizarPerfil(id, dto)
        );

        assertEquals("Perfil com id 1 não encontrado!", exception.getMessage());

        verify(perfilRepository).findById(id);
    }

    @Test
    void deveDeletarPerfilComSucesso() {
        // arrange
        Long id = 1L;

        Perfil perfil = new Perfil(id, "ADMIN", List.of());

        when(perfilRepository.findById(id)).thenReturn(Optional.of(perfil));

        // act
        perfilService.deletarPerfil(id);

        // assert
        verify(perfilRepository).findById(id);
        verify(perfilRepository).delete(perfil);
    }

    @Test
    void deveLancarExcecaoAoDeletarPerfilInexistente() {
        // arrange
        Long id = 1L;

        when(perfilRepository.findById(id)).thenReturn(Optional.empty());

        // act + assert
        EntidadeNaoEncontrada exception = assertThrows(
                EntidadeNaoEncontrada.class,
                () -> perfilService.deletarPerfil(id)
        );

        assertEquals("Perfil com id 1 não encontrado!", exception.getMessage());

        verify(perfilRepository).findById(id);
    }
}