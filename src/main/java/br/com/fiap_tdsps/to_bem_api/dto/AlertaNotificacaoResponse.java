package br.com.fiap_tdsps.to_bem_api.dto;

import br.com.fiap_tdsps.to_bem_api.model.AlertaNivel;
import br.com.fiap_tdsps.to_bem_api.model.AlertaStatus;
import java.time.LocalDateTime;

public record AlertaNotificacaoResponse(Long id, String chaveEnvio, String tipo,
        AlertaNivel nivel, AlertaStatus status, Integer etapa, Long idosoId,
        Long usuarioId, String nomeDestinatario, String emailDestinatario,
        LocalDateTime enviadoEm, LocalDateTime prazoRespostaEm) {}
