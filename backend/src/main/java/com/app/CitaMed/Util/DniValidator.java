package com.app.CitaMed.Util;

public class DniValidator {

    public static void validar(String dni) {
        if (dni == null || dni.isBlank())
            throw new IllegalArgumentException("El DNI es obligatorio");

        if (!dni.matches("\\d{8}"))
            throw new IllegalArgumentException("El DNI debe tener 8 dígitos numéricos");
    }
}
