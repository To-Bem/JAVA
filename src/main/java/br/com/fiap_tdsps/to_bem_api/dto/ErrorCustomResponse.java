package br.com.fiap_tdsps.to_bem_api.dto;

import java.time.LocalDateTime;

public record ErrorCustomResponse(
        LocalDateTime timestamp, int status, String error, String message) {
}
