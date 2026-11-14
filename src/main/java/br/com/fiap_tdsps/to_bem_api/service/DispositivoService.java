package br.com.fiap_tdsps.to_bem_api.service;

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
}
