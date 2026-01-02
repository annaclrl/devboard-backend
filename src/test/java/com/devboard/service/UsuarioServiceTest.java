package com.devboard.service;

import com.devboard.dto.usuario.UsuarioRequestDTO;
import com.devboard.model.Usuario;
import com.devboard.repository.UsuarioRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
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
    }
}