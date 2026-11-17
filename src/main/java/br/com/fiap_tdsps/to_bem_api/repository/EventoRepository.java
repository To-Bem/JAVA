package br.com.fiap_tdsps.to_bem_api.repository;

import br.com.fiap_tdsps.to_bem_api.model.Evento;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EventoRepository extends JpaRepository<Evento, Long>, org.springframework.data.jpa.repository.JpaSpecificationExecutor<Evento> {

    @EntityGraph(attributePaths = "alertas")
    Optional<Evento> findWithAlertasById(Long id);
}
