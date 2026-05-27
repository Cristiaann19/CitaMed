package com.app.CitaMed.Util;

public class EmailValidator {

    private static final String EMAIL_REGEX = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";

    public static void validar(String email) {
        if (email == null || email.isBlank())
            throw new IllegalArgumentException("El email es obligatorio");

        if (!email.matches(EMAIL_REGEX))
            throw new IllegalArgumentException("El email no tiene un formato válido");

        if (email.length() > 100)
            throw new IllegalArgumentException("El email no puede superar los 100 caracteres");
    }
}
