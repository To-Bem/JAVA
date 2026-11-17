package br.com.fiap_tdsps.to_bem_api.repository;

import br.com.fiap_tdsps.to_bem_api.model.Idoso;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IdosoRepository extends JpaRepository<Idoso, Long> {

    @EntityGraph(attributePaths = "contatos")
    Optional<Idoso> findWithContatosById(Long id);

    @EntityGraph(attributePaths = "alertas")
    Optional<Idoso> findWithAlertasById(Long id);

    @EntityGraph(attributePaths = "rotinas")
    Optional<Idoso> findWithRotinasById(Long id);

    @EntityGraph(attributePaths = "dispositivos")
    Optional<Idoso> findWithDispositivosById(Long id);
}
