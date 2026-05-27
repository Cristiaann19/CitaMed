package com.app.CitaMed.Util;

public class PasswordValidator {

    public static void validar(String password) {
        if (password == null || password.isBlank())
            throw new IllegalArgumentException("La contraseña es obligatoria");

        if (password.length() < 6)
            throw new IllegalArgumentException("La contraseña debe tener al menos 6 caracteres");

        if (password.length() > 60)
            throw new IllegalArgumentException("La contraseña no puede superar los 60 caracteres");
    }
}
