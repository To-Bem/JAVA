package br.com.fiap_tdsps.to_bem_api.service;

import br.com.fiap_tdsps.to_bem_api.model.Alerta;
import br.com.fiap_tdsps.to_bem_api.model.AlertaNivel;
import br.com.fiap_tdsps.to_bem_api.model.AlertaStatus;
import org.springframework.data.domain.Page;

public interface AlertaService {

    Alerta save(Alerta alerta);

    void delete(Long id);

    Alerta update(Long id, Alerta update);

    Page<Alerta> findAll(Integer page, Integer size);

    Alerta findById(Long id);

    Page<Alerta> findByIdosoId(Long idosoId, Integer page, Integer size);

    Page<Alerta> findByStatus(AlertaStatus status, Integer page, Integer size);

    Page<Alerta> findByNivel(AlertaNivel nivel, Integer page, Integer size);
}
