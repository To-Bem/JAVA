package br.com.fiap_tdsps.to_bem_api.repository;

import br.com.fiap_tdsps.to_bem_api.model.Dispositivo;
import br.com.fiap_tdsps.to_bem_api.model.DispositivoStatus;
import br.com.fiap_tdsps.to_bem_api.model.DispositivoTipo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DispositivoRepository extends JpaRepository<Dispositivo, Long> {

    Page<Dispositivo> findByIdosoId(Long idosoId, Pageable pageable);

    Page<Dispositivo> findByTipoAndIdosoId(DispositivoTipo tipo,Long idosoId, Pageable pageable);

    Page<Dispositivo> findByComodoAndIdosoId(String comodo, Long idosoId, Pageable pageable);

    Page<Dispositivo> findByStatusAndIdosoId(DispositivoStatus status, Long idosoId, Pageable pageable);

    @EntityGraph(attributePaths = "eventos")
    Optional<Dispositivo> findWithEventosById(Long id);
}
