package br.com.fiap_tdsps.to_bem_api.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Entity
@Setter
@Getter
@NoArgsConstructor
public class Idoso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nome;
    private String dataNascimento;
    private String telefone;
    private String endereco;
    private IdosoModo modo;

    @OneToMany(mappedBy = "idoso",fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Contato> contatos;

    @OneToMany(mappedBy = "idoso",fetch = FetchType.LAZY,cascade = CascadeType.ALL,orphanRemoval = true)
    private Set<Alerta> alertas;

    @OneToMany(mappedBy = "idoso",fetch = FetchType.LAZY,cascade = CascadeType.ALL,orphanRemoval = true)
    private Set<Rotina> rotinas;

    @OneToMany(mappedBy = "idoso",fetch = FetchType.LAZY,cascade = CascadeType.ALL,orphanRemoval = true)
    private Set<Dispositivo> dispositivos;
}
