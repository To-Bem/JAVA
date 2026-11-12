package br.com.fiap_tdsps.to_bem_api.repository;

import br.com.fiap_tdsps.to_bem_api.model.Contato;
import br.com.fiap_tdsps.to_bem_api.model.Idoso;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IdosoRepository extends JpaRepository<Idoso, Long> {

    @EntityGraph(attributePaths = {"contatos","alertas","rotinas","dispositivos"})
    Optional<Idoso> findWithAllById(Long id);

    @EntityGraph(attributePaths = "contatos")
    Optional<Contato> findWithContatosById(Long id);

    @EntityGraph(attributePaths = "alertas")
    Optional<Contato> findWithAlertasById(Long id);

    @EntityGraph(attributePaths = "rotinas")
    Optional<Contato> findWithRotinasById(Long id);

    Optional<Contato> findWithDispositivosById(Long id);
}
