package com.app.CitaMed.Util;

public class MotivoValidator {

    private static final int MAX_LENGTH = 300;

    public static void validar(String motivo) {
        if (motivo == null || motivo.isBlank())
            throw new IllegalArgumentException("El motivo de la consulta es obligatorio");

        if (motivo.length() > MAX_LENGTH)
            throw new IllegalArgumentException("El motivo no puede superar los " + MAX_LENGTH + " caracteres");
    }
}
