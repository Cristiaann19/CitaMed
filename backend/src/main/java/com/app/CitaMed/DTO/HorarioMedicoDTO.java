package com.app.CitaMed.DTO;

import com.app.CitaMed.Enums.DiaSemana;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalTime;

@Data
public class HorarioMedicoDTO {
    @NotNull(message = "Debe seleccionar un médico")
    private Long medicoId;

    @NotNull(message = "Debe seleccionar un consultorio")
    private Long consultorioId;

    @NotNull(message = "Debe indicar el día de atención")
    private DiaSemana dia;

    @NotNull(message = "Debe indicar la hora de inicio")
    private LocalTime horaInicio;

    @NotNull(message = "Debe indicar la hora de fin")
    private LocalTime horaFin;
}
