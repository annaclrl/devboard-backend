package com.devboard.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "db_usuario", uniqueConstraints = {
        @UniqueConstraint(name = "uk_db_usuario_email", columnNames = "email")
})
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "db_usuario_seq")
    @SequenceGenerator(name = "db_usuario_seq", sequenceName = "db_usuario_seq", allocationSize = 1)
    @Column(name = "id_usuario")
    private Long id;

    @Column(nullable = false, length = 200)
    private String nome;

    @Column(nullable = false, length = 250)
    private String email;

    @Column(nullable = false, length = 50)
    private String senha;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_perfil", nullable = false)
    private Perfil perfil;
}
