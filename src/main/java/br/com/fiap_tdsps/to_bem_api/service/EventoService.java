package br.com.fiap_tdsps.to_bem_api.service;

import br.com.fiap_tdsps.to_bem_api.model.Evento;
import org.springframework.data.domain.Page;

public interface EventoService {

    Evento save(Evento evento);

    void delete(Long id);

    Evento update(Long id, Evento update);

    Page<Evento> findAll(Integer page, Integer size);

    Evento findById(Long id);

    Page<Evento> findByDispositivoId(Long dispositivoId, Integer page, Integer size);

    Page<Evento> findByTipoAndDispositivoId(String tipo, Long dispositivoId, Integer page, Integer size);
}
