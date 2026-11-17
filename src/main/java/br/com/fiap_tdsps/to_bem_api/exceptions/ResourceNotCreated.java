package br.com.fiap_tdsps.to_bem_api.exceptions;

public class ResourceNotCreated extends RuntimeException {
    public ResourceNotCreated(String resource) {
        super("Não foi possivel criar a entidade de " + resource);
    }
}
