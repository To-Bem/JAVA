package br.com.fiap_tdsps.to_bem_api.service;

import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public final class Paginacao {
    private Paginacao() { }

    public static Pageable criar(int page, int size, String sort, Sort.Direction direction, Class<?> entidade) {
        try {
            var campo = entidade.getDeclaredField(sort);
            Class<?> tipo = campo.getType();
            if (!(tipo.isPrimitive() || tipo.isEnum() || tipo == String.class
                    || Number.class.isAssignableFrom(tipo) || tipo == Boolean.class
                    || tipo.getPackageName().equals("java.time"))) {
                throw new NoSuchFieldException();
            }
        } catch (NoSuchFieldException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Campo de ordenacao invalido: " + sort);
        }
        Sort ordem = Sort.by(direction, sort);
        if (!sort.equals("id")) ordem = ordem.and(Sort.by("id"));
        return PageRequest.of(page, size, ordem);
    }
}
