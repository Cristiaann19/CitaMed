package com.app.CitaMed.Util;

public class NombreValidator {

    private static final int MAX_LENGTH = 80;

    public static void validar(String nombre, String campo) {
        if (nombre == null || nombre.isBlank())
            throw new IllegalArgumentException("El " + campo + " es obligatorio");

        if (nombre.length() > MAX_LENGTH)
            throw new IllegalArgumentException("El " + campo + " no puede superar los " + MAX_LENGTH + " caracteres");

        if (!nombre.matches("[a-zA-ZáéíóúÁÉÍÓÚñÑüÜ'\\s]+"))
            throw new IllegalArgumentException("El " + campo + " solo puede contener letras y espacios");
    }
}
