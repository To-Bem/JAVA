package br.com.fiap_tdsps.to_bem_api.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Dispositivo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private DispositivoTipo tipo;
    private String comodo;
    private DispositivoStatus status;
    private LocalDateTime ultimoHeartbeat;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idoso_id")
    private Idoso idoso;

    @OneToMany(mappedBy = "dispositivo",fetch = FetchType.LAZY,cascade = CascadeType.ALL,orphanRemoval = true)
    private Set<Evento> eventos;
}
