package br.com.fiap_tdsps.to_bem_api.service;

import br.com.fiap_tdsps.to_bem_api.model.Rotina;
import org.springframework.data.domain.Page;

public interface RotinaService {

    Rotina save(Rotina rotina);

    void delte(Long id);

    Rotina update(Long id, Rotina update);

    Page<Rotina> findAll(Integer page, Integer size);

    Rotina findById(Long id);

    Page<Rotina> findByIdosoId(Long idosoId, Integer page, Integer size);
}
