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

    Page<Alerta> findAll(Integer page, Integer size);

    Alerta findById(Long id);

    Page<Alerta> findByIdosoId(Long idosoId, Integer page, Integer size);

    Page<Alerta> findByStatusAndOtherId(AlertaStatus status, Long idosoId, Long usuarioId, Integer page, Integer size);

    Page<Alerta> findByNivelAndOtherId(AlertaNivel nivel,Long idosoId, Long usuarioId, Integer page, Integer size);

    Page<Alerta> findByUsuarioId(Long usuarioId, Integer page, Integer size);

    Page<Alerta> findAll(Pageable pageable);
    Page<Alerta> findByIdosoId(Long idosoId, Pageable pageable);
    Page<Alerta> findByStatusAndOtherId(AlertaStatus status,Long idosoId,Long usuarioId, Pageable pageable);
    Page<Alerta> findByNivelAndOtherId(AlertaNivel nivel, Long idosoId,Long usuarioId , Pageable pageable);
    Page<Alerta> findByUsuarioId(Long usuarioId, Pageable pageable);
    Page<Alerta> findByOtherFilter(Long idosoId, Long usuarioId, AlertaStatus status, AlertaNivel nivel, Pageable pageable);
}
