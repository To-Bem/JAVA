package br.com.fiap_tdsps.to_bem_api.service;

import br.com.fiap_tdsps.to_bem_api.model.Contato;
import br.com.fiap_tdsps.to_bem_api.model.Idoso;
import org.springframework.data.domain.Page;

import java.util.Optional;


public interface IdosoService {

    Idoso save(Idoso idoso);

    void delete(Long id);

    Idoso update(Long id, Idoso update);

    Page<Idoso> findAll(Integer page, Integer size);

    Idoso findById(Long id);

    Idoso findWithAllById(Long id);

    Contato findWithContatosById(Long id);

    Contato findWithAlertasById(Long id);

    Contato findWithRotinasById(Long id);

    Contato findWithDispositivosById(Long id);
}
