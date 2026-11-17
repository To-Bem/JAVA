package br.com.fiap_tdsps.to_bem_api.service;

import org.springframework.data.domain.Pageable;

import br.com.fiap_tdsps.to_bem_api.model.Evento;
import org.springframework.data.domain.Page;

public interface EventoService {

    Evento save(Evento evento);

    void delete(Long id);

    Evento update(Long id, Evento update);

    Evento findById(Long id);

    Evento findWithAlertasById(Long id);

    Page<Evento> findAll(Pageable pageable);
    Page<Evento> findByOtherFilter(Long dispositivoId, String tipo, Pageable pageable);
}
