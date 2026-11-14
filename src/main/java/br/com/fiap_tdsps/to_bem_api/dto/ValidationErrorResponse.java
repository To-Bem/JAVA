package br.com.fiap_tdsps.to_bem_api.dto;

import java.time.LocalDateTime;
import java.util.List;

public record ValidationErrorResponse(LocalDateTime timestamp, int status, String error, List<ErrorFieldCustom> errors) {
}
