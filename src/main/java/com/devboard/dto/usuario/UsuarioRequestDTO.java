package com.devboard.dto.usuario;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UsuarioRequestDTO {

    @NotBlank(message = "O nome é obrigatório")
    @Size(min = 3, max = 200, message = "O nome deve ter entre 3 a 200 caracteres")
    private String nome;

    @NotBlank(message = "O email é obrigatório")
    @Size(max = 250, message = "O email deve ter até 250 caracteres")
    @Email(message = "E-mail no formato inválido")
    private String email;

    @NotBlank(message = "A senha é obrigatória")
    @Size(min = 6, max = 50, message = "A senha deve ter entre 6 a 50 caracteres")
    private String senha;

}
