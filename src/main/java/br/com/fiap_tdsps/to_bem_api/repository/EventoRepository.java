package br.com.fiap_tdsps.to_bem_api.repository;

import br.com.fiap_tdsps.to_bem_api.model.Evento;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventoRepository extends JpaRepository<Evento, Long> {

    Page<Evento> findByDispositivoId(Long dispositivoId, Pageable pageable);

    Page<Evento> findByTipo(String tipo,Pageable pageable);
}
