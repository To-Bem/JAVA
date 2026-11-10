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

    Page<Dispositivo> findByTipo(DispositivoTipo tipo, Integer page, Integer size);

    Page<Dispositivo> findByComodo(String comodo, Integer page, Integer size);

    Page<Dispositivo> findByStatus(DispositivoStatus status, Integer page, Integer size);
}
