package com.app.CitaMed.Util;

public class UserNameValidator {

    public static void validar(String userName) {
        if (userName == null || userName.isBlank())
            throw new IllegalArgumentException("El nombre de usuario es obligatorio");

        if (userName.length() < 4 || userName.length() > 20)
            throw new IllegalArgumentException("El nombre de usuario debe tener entre 4 y 20 caracteres");

        if (!userName.matches("[a-zA-Z0-9._]+"))
            throw new IllegalArgumentException("El nombre de usuario solo puede contener letras, números, puntos y guiones bajos");
    }
}
