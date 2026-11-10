package br.com.fiap_tdsps.to_bem_api.service;

import br.com.fiap_tdsps.to_bem_api.model.Idoso;
import org.springframework.data.domain.Page;

public interface IdosoService {

    Idoso save(Idoso idoso);

    void delete(Long id);

    Idoso update(Long id, Idoso update);

    Page<Idoso> findAll(Integer page, Integer size);

    Idoso findById(Long id);
}
