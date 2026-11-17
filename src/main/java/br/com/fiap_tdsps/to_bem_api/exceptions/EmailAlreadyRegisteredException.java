package br.com.fiap_tdsps.to_bem_api.exceptions;

public class EmailAlreadyRegisteredException extends RuntimeException {

    public EmailAlreadyRegisteredException(String email) {
        super("Já existe um colaborador cadastrado com o email " + email);
    }
}
