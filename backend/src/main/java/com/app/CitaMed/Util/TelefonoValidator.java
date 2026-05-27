package com.app.CitaMed.Util;

public class TelefonoValidator {

    public static void validar(String telefono) {
        if (telefono == null || telefono.isBlank())
            throw new IllegalArgumentException("El teléfono es obligatorio");

        if (!telefono.matches("\\d{7,15}"))
            throw new IllegalArgumentException("El teléfono debe tener entre 7 y 15 dígitos");
    }
}
