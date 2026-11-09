package br.com.fiap_tdsps.to_bem_api.repository;

import br.com.fiap_tdsps.to_bem_api.model.Dispositivo;
import br.com.fiap_tdsps.to_bem_api.model.DispositivoStatus;
import br.com.fiap_tdsps.to_bem_api.model.DispositivoTipo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DispositivoRepository extends JpaRepository<Dispositivo, Long> {

    Page<Dispositivo> findByIdosoId(Long idosoId, Pageable pageable);

    Page<Dispositivo> findByTipo(DispositivoTipo tipo, Pageable pageable);

    Page<Dispositivo> findByComodo(String comodo, Pageable pageable);

    Page<Dispositivo> findByStatus(DispositivoStatus status, Pageable pageable);
}
