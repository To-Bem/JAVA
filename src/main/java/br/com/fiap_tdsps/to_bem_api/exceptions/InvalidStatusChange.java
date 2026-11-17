package br.com.fiap_tdsps.to_bem_api.exceptions;

public class InvalidStatusChange extends RuntimeException {
    public InvalidStatusChange(String resource, String status) {
        super("O status " + status + " não pode ser alterado em " + resource);
    }
}
