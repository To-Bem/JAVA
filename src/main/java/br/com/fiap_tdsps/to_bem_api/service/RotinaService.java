package br.com.fiap_tdsps.to_bem_api.service;

import org.springframework.data.domain.Pageable;

import br.com.fiap_tdsps.to_bem_api.model.Rotina;
import org.springframework.data.domain.Page;

public interface RotinaService {

    Rotina save(Rotina rotina);

    void delete(Long id);

    Rotina update(Long id, Rotina update);

    Rotina findById(Long id);

    Page<Rotina> findAll(Pageable pageable);
    Page<Rotina> findByIdosoId(Long idosoId, Pageable pageable);
}
