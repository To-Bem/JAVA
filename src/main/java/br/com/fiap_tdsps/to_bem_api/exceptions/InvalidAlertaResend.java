package br.com.fiap_tdsps.to_bem_api.exceptions;

public class InvalidAlertaResend extends RuntimeException{

    public  InvalidAlertaResend(){
        super("Não foi possível reenviar o alerta. Verifique se ele está em um estado válido para reenvio.");
    }
}
