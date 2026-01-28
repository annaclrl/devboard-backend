package com.devboard.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "db_perfil")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Perfil {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "db_perfil_seq")
    @SequenceGenerator(name = "db_perfil_seq", sequenceName = "db_perfil_seq", allocationSize = 1)
    @Column(name = "id_perfil")
    private Long id;

    @Column(nullable = false, length = 100)
    private String descricao;

    @OneToMany(
            mappedBy = "perfil",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<Usuario> usuarios = new ArrayList<>();
}
