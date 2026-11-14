package br.com.fiap_tdsps.to_bem_api.model;

import java.time.format.DateTimeFormatter;

public final class CommonVariables {

    private CommonVariables(){}

    public static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    public static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public static final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
}
