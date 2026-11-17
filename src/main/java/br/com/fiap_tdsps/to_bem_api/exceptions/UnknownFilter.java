package br.com.fiap_tdsps.to_bem_api.exceptions;

public class UnknownFilter extends RuntimeException{

    public UnknownFilter(String resource){
        super("Combinação de filtros inválida para " + resource + ". Verifique os parâmetros informados.");
    }
}
