package br.com.fiap_tdsps.to_bem_api.service;

import org.springframework.data.domain.Sort;

import org.springframework.data.domain.PageRequest;

import br.com.fiap_tdsps.to_bem_api.exceptions.ResourceNotFoundException;
import br.com.fiap_tdsps.to_bem_api.model.CommonVariables;
import br.com.fiap_tdsps.to_bem_api.model.Contato;
import br.com.fiap_tdsps.to_bem_api.model.Idoso;
import br.com.fiap_tdsps.to_bem_api.model.Usuario;
import br.com.fiap_tdsps.to_bem_api.repository.IdosoRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class IdosoServiceImpl implements  IdosoService{

    private final IdosoRepository repository;
    private final UsuarioService usuarioService;
    private final ContatoService contatoService;

    public IdosoServiceImpl(IdosoRepository repository, UsuarioService usuarioService,
                            ContatoService contatoService){
        this.repository = repository;
        this.usuarioService = usuarioService;
        this.contatoService = contatoService;
    }

    @Override
    public Idoso save(Idoso idoso) {
        return this.repository.save(idoso);
    }

    @Override
    public void delete(Long id) {
        this.repository.delete(findById(id));
    }

    @Override
    public Idoso update(Long id, Idoso update) {
        update.setId(id);
        return this.repository.save(update);
    }

    @Override
    public Page<Idoso> findAll(Integer page, Integer size) {
        return this.findAll(PageRequest.of(page, size, Sort.by("id")));
    }

    @Override
    public Page<Idoso> findAll(Pageable pageable) {
        return this.repository.findAll(pageable);
    }

    @Override
    public Idoso findById(Long id) {
        return this.repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("idoso", id));
    }

    @Override
    @org.springframework.transaction.annotation.Transactional(readOnly = true)
    public Idoso findWithAllById(Long id) {
        Idoso idoso = this.findById(id);
        idoso.getContatos().size();
        idoso.getAlertas().size();
        idoso.getRotinas().size();
        idoso.getDispositivos().size();
        return idoso;
    }

    @Override
    public Idoso findWithContatosById(Long id) {
        return this.repository.findWithContatosById(id).orElseThrow(() -> new ResourceNotFoundException("idoso", id));
    }

    @Override
    public Idoso findWithAlertasById(Long id) {
        return this.repository.findWithAlertasById(id).orElseThrow(() -> new ResourceNotFoundException("idoso", id));
    }

    @Override
    public Idoso findWithRotinasById(Long id) {
        return this.repository.findWithRotinasById(id).orElseThrow(() -> new ResourceNotFoundException("idoso", id));
    }

    @Override
    public Idoso findWithDispositivosById(Long id) {
        return this.repository.findWithDispositivosById(id).orElseThrow(() -> new ResourceNotFoundException("idoso", id));
    }

    @Transactional
    @Override
    public Idoso cadastrarComContato(Long usuarioId,Idoso idoso,String parentesco,Integer ordemEscalonamento) {
        Usuario usuario = this.usuarioService.findById(usuarioId);

        Idoso salvo = this.repository.save(idoso);

        Contato contato = new Contato();
        contato.setUsuario(usuario);
        contato.setIdoso(salvo);
        contato.setParentesco(parentesco);
        contato.setOrdemEscalonamento(ordemEscalonamento);

        this.contatoService.save(contato);

        return salvo;
    }
}