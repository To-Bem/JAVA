package br.com.fiap_tdsps.to_bem_api.exceptions;

public class MoreThanOneFilter extends RuntimeException{

    public MoreThanOneFilter(){
        super("Mais de um filtro foi informado");
    }
}
