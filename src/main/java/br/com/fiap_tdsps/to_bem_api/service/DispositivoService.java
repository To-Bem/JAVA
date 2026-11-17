package br.com.fiap_tdsps.to_bem_api.service;

import org.springframework.data.domain.Pageable;

import br.com.fiap_tdsps.to_bem_api.model.Dispositivo;
import br.com.fiap_tdsps.to_bem_api.model.DispositivoStatus;
import br.com.fiap_tdsps.to_bem_api.model.DispositivoTipo;
import org.springframework.data.domain.Page;


public interface DispositivoService {

    Dispositivo save(Dispositivo dispositivo);

    void delete(Long id);

    Dispositivo update(Long id, Dispositivo update);

    Page<Dispositivo> findAll(Integer page, Integer size);

    Dispositivo findById(Long id);

    Page<Dispositivo> findByIdosoId(Long idosoId, Integer page, Integer size);

    Page<Dispositivo> findByTipoAndIdosoId(DispositivoTipo tipo, Long idosoId, Integer page, Integer size);

    Page<Dispositivo> findByComodoAndIdosoId(String comodo, Long idosoId, Integer page, Integer size);

    Page<Dispositivo> findByStatusAndIdosoId(DispositivoStatus status, Long idosoId, Integer page, Integer size);

    Dispositivo findWithEventosById(Long id);

    Dispositivo updateHeartBeatById(Long id,String updateHeartbeat);

    Page<Dispositivo> findAll(Pageable pageable);
    Page<Dispositivo> findByIdosoId(Long idosoId, Pageable pageable);
    Page<Dispositivo> findByTipoAndIdosoId(DispositivoTipo tipo,Long idosoId, Pageable pageable);
    Page<Dispositivo> findByComodoAndIdosoId(String comodo, Long idosoId, Pageable pageable);
    Page<Dispositivo> findByStatusAndIdosoId(DispositivoStatus status, Long idosoId, Pageable pageable);
    Page<Dispositivo> findByOtherFilter(Long idosoId, DispositivoTipo tipo, String comodo, DispositivoStatus status, Pageable pageable);
}
