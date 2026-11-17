package br.com.fiap_tdsps.to_bem_api.service;

import br.com.fiap_tdsps.to_bem_api.dto.AlertaNotificacaoResponse;
import br.com.fiap_tdsps.to_bem_api.exceptions.ResourceNotFoundException;
import br.com.fiap_tdsps.to_bem_api.model.*;
import br.com.fiap_tdsps.to_bem_api.repository.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import java.time.Duration;
import java.time.LocalDateTime;

@Service
public class AlertaNotificacaoService {

    private final AlertaRepository repository;
    private final ContatoRepository contatoRepository;
    private final Duration prazoResposta;

    public AlertaNotificacaoService(AlertaRepository repository, ContatoRepository contatoRepository,
            @Value("${alertas.prazo-resposta:PT5M}") Duration prazoResposta) {
        if (prazoResposta.isZero() || prazoResposta.isNegative()) throw new IllegalArgumentException("Prazo deve ser positivo");
        this.repository = repository;
        this.contatoRepository = contatoRepository;
        this.prazoResposta = prazoResposta;
    }

    @Transactional(readOnly = true)
    public Page<AlertaNotificacaoResponse> findPendentesEnvio(int page, int size) {
        return this.repository.findPendentesEnvio(PageRequest.of(page, size)).map(this::toResponse);
    }

    @Transactional(readOnly = true)
    public Page<AlertaNotificacaoResponse> findVencidos(int page, int size) {
        return this.repository.findVencidos(LocalDateTime.now(), PageRequest.of(page, size)).map(this::toResponse);
    }

    @Transactional
    public AlertaNotificacaoResponse confirmarEnvio(Long id, Integer etapa) {
        Alerta alerta = this.findById(id);
        this.validarEtapa(alerta, etapa);
        // Repetir a confirmacao nao renova o prazo de resposta.
        if (alerta.getEnviadoEm() == null) {
            LocalDateTime agora = LocalDateTime.now();
            alerta.setEnviadoEm(agora);
            alerta.setPrazoRespostaEm(agora.plus(this.prazoResposta));
        }
        return this.toResponse(alerta);
    }

    @Transactional
    public AlertaNotificacaoResponse escalonar(Long id, Integer etapa) {
        Alerta alerta = this.findById(id);
        this.validarEtapa(alerta, etapa);
        if (alerta.getPrazoRespostaEm() == null || alerta.getPrazoRespostaEm().isAfter(LocalDateTime.now())) {
            throw this.conflito("O prazo de resposta ainda nao expirou ou o envio nao foi confirmado.");
        }
        Contato proximoContato = this.contatoRepository.findFirstByIdosoIdAndOrdemEscalonamentoGreaterThanOrderByOrdemEscalonamentoAsc(
                alerta.getIdoso().getId(), alerta.getNivelEscalonamento()).orElse(null);
        if (proximoContato == null) {
            // Mantem o alerta pendente, sem voltar a aparecer na lista de vencidos.
            alerta.setPrazoRespostaEm(null);
        } else {
            alerta.setUsuario(proximoContato.getUsuario());
            alerta.setNivelEscalonamento(proximoContato.getOrdemEscalonamento());
            alerta.setStatus(AlertaStatus.ESCALONADO);
            alerta.setEnviadoEm(null);
            alerta.setPrazoRespostaEm(null);
        }
        return this.toResponse(alerta);
    }

    private Alerta findById(Long id) {
        return this.repository.findForUpdate(id).orElseThrow(() -> new ResourceNotFoundException("alerta", id));
    }

    private void validarEtapa(Alerta alerta, Integer etapa) {
        if (alerta.getStatus() == AlertaStatus.RESOLVIDO) throw this.conflito("O alerta ja foi resolvido.");
        if (!java.util.Objects.equals(alerta.getNivelEscalonamento(), etapa)) {
            throw this.conflito("A etapa do alerta mudou. Consulte novamente os dados.");
        }
    }

    private ResponseStatusException conflito(String mensagem) {
        return new ResponseStatusException(HttpStatus.CONFLICT, mensagem);
    }

    private AlertaNotificacaoResponse toResponse(Alerta alerta) {
        Usuario usuario = alerta.getUsuario();
        return new AlertaNotificacaoResponse(alerta.getId(), alerta.getId() + ":" + alerta.getNivelEscalonamento(),
                alerta.getTipo(), alerta.getNivel(), alerta.getStatus(), alerta.getNivelEscalonamento(),
                alerta.getIdoso().getId(), usuario.getId(), usuario.getNome(), usuario.getEmail(), alerta.getEnviadoEm(), alerta.getPrazoRespostaEm());
    }
}
