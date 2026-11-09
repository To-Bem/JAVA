package br.com.fiap_tdsps.to_bem_api.repository;

import br.com.fiap_tdsps.to_bem_api.model.Alerta;
import br.com.fiap_tdsps.to_bem_api.model.AlertaNivel;
import br.com.fiap_tdsps.to_bem_api.model.AlertaStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AlertaRepository extends JpaRepository<Alerta, Long> {

    Page<Alerta> findByIdosoId(Long idosoId, Pageable pageable);

    Page<Alerta> findByStatus(AlertaStatus status, Pageable pageable);

    Page<Alerta> findByNivel(AlertaNivel nivel, Pageable pageable);
}
