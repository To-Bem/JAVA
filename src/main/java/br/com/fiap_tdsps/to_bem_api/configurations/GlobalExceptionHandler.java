package br.com.fiap_tdsps.to_bem_api.configurations;

import br.com.fiap_tdsps.to_bem_api.dto.ErrorCustomResponse;
import br.com.fiap_tdsps.to_bem_api.dto.ErrorFieldCustom;
import br.com.fiap_tdsps.to_bem_api.dto.ValidationErrorResponse;
import br.com.fiap_tdsps.to_bem_api.exceptions.*;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorCustomResponse> handleException(ResourceNotFoundException ex){
        return build(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ErrorCustomResponse> handleAuthentication(AuthenticationException ex) {
        return build(HttpStatus.UNAUTHORIZED, "Credenciais inválidas");
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorCustomResponse> handleAccessDenied(AccessDeniedException ex) {
        return build(HttpStatus.FORBIDDEN, "Você não tem permissão para executar esta ação");
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorCustomResponse> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
        return build(HttpStatus.BAD_REQUEST, "Parâmetro '" + ex.getName() + "' inválido: " + ex.getValue());
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorCustomResponse> handleConstraintViolation(ConstraintViolationException ex) {
        String message = ex.getConstraintViolations().stream()
                .map(v -> v.getPropertyPath() + ": " + v.getMessage())
                .collect(Collectors.joining("; "));
        return build(HttpStatus.BAD_REQUEST, message);
    }

    @ExceptionHandler(UnknownFilter.class)
    public ResponseEntity<ErrorCustomResponse> handleMoreThanOneFilter(UnknownFilter ex){
        return build(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    @ExceptionHandler(InvalidAlertaResend.class)
    public ResponseEntity<ErrorCustomResponse> handleInvalidAlertaResend(InvalidAlertaResend ex){
        return build(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ValidationErrorResponse> handleArgumentNotValidException(MethodArgumentNotValidException ex){

        List<ErrorFieldCustom> fieldErrors = ex.getBindingResult().getFieldErrors().stream()
                .map(fieldError -> new ErrorFieldCustom(fieldError.getField(), fieldError.getDefaultMessage()))
                .toList();

        List<ErrorFieldCustom> globalErrors = ex.getBindingResult().getGlobalErrors().stream()
                .map(globalError -> new ErrorFieldCustom(globalError.getObjectName(), globalError.getDefaultMessage()))
                .toList();

        List<ErrorFieldCustom> errors = Stream.concat(fieldErrors.stream(), globalErrors.stream()).toList();

        ValidationErrorResponse body = new ValidationErrorResponse(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                errors
        );
        return ResponseEntity.badRequest().body(body);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorCustomResponse> handleNotReadable(HttpMessageNotReadableException ex) {
        return build(HttpStatus.BAD_REQUEST, "Corpo da requisição inválido ou malformado");
    }

    @ExceptionHandler(RoleNotPermitted.class)
    public ResponseEntity<ErrorCustomResponse> handleRoleNotPermitted(RoleNotPermitted ex){
        return build(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ErrorCustomResponse> handleMissingServletRequestParameterException(MissingServletRequestParameterException ex){
        return build(HttpStatus.BAD_REQUEST, "O parâmetro obrigatório '" + ex.getParameterName() + "' não foi informado.");
    }
    @ExceptionHandler(ResourceNotCreated.class)
    public ResponseEntity<ErrorCustomResponse> handleEntityNotCreated(ResourceNotCreated ex){
        return build(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
    }

    @ExceptionHandler(org.springframework.web.server.ResponseStatusException.class)
    public ResponseEntity<ErrorCustomResponse> handleResponseStatus(org.springframework.web.server.ResponseStatusException ex) {
        return build(HttpStatus.valueOf(ex.getStatusCode().value()), ex.getReason());
    }

    @ExceptionHandler(org.springframework.dao.DataIntegrityViolationException.class)
    public ResponseEntity<ErrorCustomResponse> handleIntegridade(org.springframework.dao.DataIntegrityViolationException ex) {
        log.warn("Violacao de integridade no banco", ex);
        return build(HttpStatus.CONFLICT, "Dados duplicados, obrigatorios ausentes ou registro vinculado a outro recurso.");
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorCustomResponse> handleGeneric(Exception ex) {
        log.error("Erro inesperado ao processar requisicao", ex);
        return build(HttpStatus.INTERNAL_SERVER_ERROR, "Erro interno inesperado");
    }

    private ResponseEntity<ErrorCustomResponse> build(HttpStatus status, String message) {
        return ResponseEntity
                .status(status)
                .body(new ErrorCustomResponse(LocalDateTime.now(), status.value(), status.getReasonPhrase(), message));
    }
}