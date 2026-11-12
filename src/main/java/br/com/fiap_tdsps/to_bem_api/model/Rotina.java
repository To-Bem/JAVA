package br.com.fiap_tdsps.to_bem_api.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Rotina {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String diaSemana;
    private LocalTime horaPrimeiraAtividade;
    private Integer mediaLevantadasPorNoite;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idoso_id")
    private Idoso idoso;
}
