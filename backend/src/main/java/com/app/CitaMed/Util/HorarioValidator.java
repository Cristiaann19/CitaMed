package com.app.CitaMed.Util;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class HorarioValidator {
    private static final LocalTime HORA_INICIO = LocalTime.of(8, 0);
    private static final LocalTime HORA_FIN = LocalTime.of(18, 0);

    public static void validar(LocalDateTime fechaHora) {
        DayOfWeek dia = fechaHora.getDayOfWeek();

        if (dia == DayOfWeek.SATURDAY || dia == DayOfWeek.SUNDAY) {
            throw new IllegalArgumentException(
                    "Solo se puede reservar de lunes a viernes");
        }

        LocalTime hora = fechaHora.toLocalTime();
        if (hora.isBefore(HORA_INICIO) || hora.isAfter(HORA_FIN)) {
            throw new IllegalArgumentException(
                    "Solo se puede reservar de 8:00 AM a 6:00 PM");
        }
    }
}
