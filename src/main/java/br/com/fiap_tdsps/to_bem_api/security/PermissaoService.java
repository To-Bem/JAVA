package br.com.fiap_tdsps.to_bem_api.security;

import br.com.fiap_tdsps.to_bem_api.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("permissaoService")
@Transactional(readOnly = true)
public class PermissaoService {
    private final ContatoRepository contatoRepository;
    private final DispositivoRepository dispositivoRepository;
    private final EventoRepository eventoRepository;
    private final AlertaRepository alertaRepository;
    private final RotinaRepository rotinaRepository;

    public PermissaoService(ContatoRepository contatoRepository, DispositivoRepository dispositivoRepository,
                            EventoRepository eventoRepository, AlertaRepository alertaRepository,
                            RotinaRepository rotinaRepository) {
        this.contatoRepository = contatoRepository;
        this.dispositivoRepository = dispositivoRepository;
        this.eventoRepository = eventoRepository;
        this.alertaRepository = alertaRepository;
        this.rotinaRepository = rotinaRepository;
    }

    public boolean proprioUsuario(Long usuarioId) {
        return usuarioId != null && usuarioId.equals(SecurityUtils.currentUsuario().getId());
    }

    public boolean podeAcessarIdoso(Long idosoId) {
        return idosoId != null && this.contatoRepository.existsByIdosoIdAndUsuarioId(
                idosoId, SecurityUtils.currentUsuario().getId());
    }

    public boolean podeAcessarContato(Long id) {
        return id != null && this.contatoRepository.findById(id)
                .map(contato -> this.podeAcessarIdoso(contato.getIdoso().getId())).orElse(false);
    }

    public boolean podeAcessarDispositivo(Long id) {
        return id != null && this.dispositivoRepository.findById(id)
                .map(dispositivo -> this.podeAcessarIdoso(dispositivo.getIdoso().getId())).orElse(false);
    }

    public boolean podeAcessarEvento(Long id) {
        return id != null && this.eventoRepository.findById(id)
                .map(evento -> this.podeAcessarDispositivo(evento.getDispositivo().getId())).orElse(false);
    }

    public boolean podeAcessarAlerta(Long id) {
        return id != null && this.alertaRepository.findById(id)
                .map(alerta -> this.podeAcessarIdoso(alerta.getIdoso().getId())).orElse(false);
    }

    public boolean podeAcessarRotina(Long id) {
        return id != null && this.rotinaRepository.findById(id)
                .map(rotina -> this.podeAcessarIdoso(rotina.getIdoso().getId())).orElse(false);
    }

    public boolean podeFiltrar(Long idosoId, Long usuarioId) {
        if (usuarioId != null && !this.proprioUsuario(usuarioId)) return false;
        if (idosoId != null) return this.podeAcessarIdoso(idosoId);
        return this.proprioUsuario(usuarioId);
    }
}
