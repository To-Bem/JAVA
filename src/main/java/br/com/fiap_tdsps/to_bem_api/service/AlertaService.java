package br.com.fiap_tdsps.to_bem_api.service;

import org.springframework.data.domain.Pageable;

import br.com.fiap_tdsps.to_bem_api.model.Alerta;
import br.com.fiap_tdsps.to_bem_api.model.AlertaNivel;
import br.com.fiap_tdsps.to_bem_api.model.AlertaStatus;
import org.springframework.data.domain.Page;

public interface AlertaService {

    Alerta save(Alerta alerta);

    void delete(Long id);

    Alerta update(Long id, Alerta update);

    Alerta findById(Long id);

    Page<Alerta> findAll(Pageable pageable);
    Page<Alerta> findByOtherFilter(Long idosoId, Long usuarioId, AlertaStatus status, AlertaNivel nivel, Pageable pageable);
}
