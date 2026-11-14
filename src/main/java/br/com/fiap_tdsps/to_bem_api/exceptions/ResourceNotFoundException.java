package br.com.fiap_tdsps.to_bem_api.exceptions;

public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String resource, Long id) {
        super(resource + " com id "+id+" não encontrado");
    }
}
