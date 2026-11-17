package br.com.fiap_tdsps.to_bem_api.service;

import org.springframework.data.domain.Pageable;

import br.com.fiap_tdsps.to_bem_api.model.Idoso;
import org.springframework.data.domain.Page;

public interface IdosoService {

    void delete(Long id);

    Idoso update(Long id, Idoso update);

    Idoso findById(Long id);

    Idoso findWithAllById(Long id);

    Idoso findWithContatosById(Long id);

    Idoso findWithAlertasById(Long id);

    Idoso findWithRotinasById(Long id);

    Idoso findWithDispositivosById(Long id);

    public Idoso cadastrarComContato(Long usuarioId, Idoso idoso, String parentesco, Integer ordemEscalonamento);

    Page<Idoso> findAll(Pageable pageable);
}
