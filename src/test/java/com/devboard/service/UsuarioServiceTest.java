package com.devboard.service;

import com.devboard.dto.usuario.UsuarioRequestDTO;
import com.devboard.dto.usuario.UsuarioResponseDTO;
import com.devboard.exception.EntidadeNaoEncontrada;
import com.devboard.model.Usuario;
import com.devboard.repository.UsuarioRepository;
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
class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private UsuarioService usuarioService;

    @Test
    void deveSalvarUsuario() {
        // arrange - configurar os dados ( instanciar objs, definir mocks e preparar estado inicial de teste )

        UsuarioRequestDTO usuario = new UsuarioRequestDTO("Anna", "anna@email.com", "123456");
        Usuario usuarioSalvo = new Usuario(1L, "Anna", "anna@email.com", "123456");
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuarioSalvo);

        // act - executar apenas a ação testada

        var resultado = usuarioService.salvarUsuario(usuario);

        // assert - validar resultados

        assertEquals(1L, resultado.getId());
        verify(usuarioRepository).save(any(Usuario.class));
    }

    @Test
    void deveListarTodosUsuarios() {
        // arrange
        List<Usuario> usuarios = List.of(
                new Usuario(1L, "Felipe", "felipe@email.com", "123456"),
                new Usuario(2L, "Anna", "anna@email.com", "123456")
        );

        when(usuarioRepository.findAll()).thenReturn(usuarios);

        // act
        List<UsuarioResponseDTO> resultado = usuarioService.listarTodosUsuarios();

        // assert
        assertEquals(2, resultado.size());
        assertEquals("Felipe", resultado.get(0).getNome());
        verify(usuarioRepository).findAll();
    }

    @Test
    void deveBuscarUsuarioPorIdComSucesso() {
        // arrange
        Long id = 1L;

        Usuario usuario = new Usuario(id, "Felipe", "felipe@email.com", "123456");

        when(usuarioRepository.findById(id)).thenReturn(Optional.of(usuario));

        // act
        UsuarioResponseDTO resultado = usuarioService.buscarUsuarioPorId(id);

        // assert
        assertNotNull(resultado);
        assertEquals(id, resultado.getId());
        assertEquals("Felipe", resultado.getNome());
        assertEquals("felipe@email.com", resultado.getEmail());

        verify(usuarioRepository).findById(id);
    }

    @Test
    void deveLancarExcecaoQuandoUsuarioNaoEncontrado() {
        // arrange
        Long id = 1L;

        when(usuarioRepository.findById(id)).thenReturn(Optional.empty());

        // act + assert
        EntidadeNaoEncontrada exception = assertThrows(
                EntidadeNaoEncontrada.class,
                () -> usuarioService.buscarUsuarioPorId(id)
        );

        assertEquals("Usuário com id 1 não encontrado!", exception.getMessage());

        verify(usuarioRepository).findById(id);
    }

    @Test
    void deveAtualizarUsuarioComSucesso() {
        // arrange
        Long id = 1L;

        Usuario usuarioExistente = new Usuario(
                id,
                "Felipe",
                "felipe@email.com",
                "123456"
        );

        UsuarioRequestDTO dtoAtualizacao = new UsuarioRequestDTO(
                "Felipe Atualizado",
                "felipe.novo@email.com",
                "654321"
        );

        when(usuarioRepository.findById(id)).thenReturn(Optional.of(usuarioExistente));

        when(usuarioRepository.save(any(Usuario.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // act
        UsuarioResponseDTO resultado = usuarioService.atualizarUsuario(id, dtoAtualizacao);

        // assert
        assertNotNull(resultado);
        assertEquals(id, resultado.getId());
        assertEquals("Felipe Atualizado", resultado.getNome());
        assertEquals("felipe.novo@email.com", resultado.getEmail());

        verify(usuarioRepository).findById(id);
        verify(usuarioRepository).save(usuarioExistente);
    }

    @Test
    void deveLancarExcecaoAoAtualizarUsuarioInexistente() {
        // arrange
        Long id = 1L;

        UsuarioRequestDTO dto = new UsuarioRequestDTO(
                "Novo Nome",
                "novo@email.com",
                "123456"
        );

        when(usuarioRepository.findById(id)).thenReturn(Optional.empty());

        // act + assert
        EntidadeNaoEncontrada exception = assertThrows(
                EntidadeNaoEncontrada.class,
                () -> usuarioService.atualizarUsuario(id, dto)
        );

        assertEquals("Usuário com id 1 não encontrado!", exception.getMessage());

        verify(usuarioRepository).findById(id);
    }
}