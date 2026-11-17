package br.com.fiap_tdsps.to_bem_api.repository;

import br.com.fiap_tdsps.to_bem_api.model.Dispositivo;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DispositivoRepository extends JpaRepository<Dispositivo, Long>, org.springframework.data.jpa.repository.JpaSpecificationExecutor<Dispositivo> {

    @EntityGraph(attributePaths = "eventos")
    Optional<Dispositivo> findWithEventosById(Long id);
}
