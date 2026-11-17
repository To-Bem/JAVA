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

    Dispositivo findById(Long id);

    Dispositivo findWithEventosById(Long id);

    Dispositivo updateHeartBeatById(Long id,String updateHeartbeat);

    Page<Dispositivo> findAll(Pageable pageable);
    Page<Dispositivo> findByOtherFilter(Long idosoId, DispositivoTipo tipo, String comodo, DispositivoStatus status, Pageable pageable);
}
