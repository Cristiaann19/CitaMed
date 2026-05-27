package com.app.CitaMed.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CitaDTO {
    @NotNull(message = "Debe seleccionar un paciente")
    private Long pacienteId;

    @NotNull(message = "Debe seleccionar un médico")
    private Long medicoId;

    @NotNull(message = "La fecha y hora de la cita es obligatoria")
    private LocalDateTime fechaHora;

    @NotBlank(message = "Debe indicar el motivo de la consulta")
    @Size(max = 200, message = "El motivo no puede superar los 200 caracteres")
    private String motivoConsulta;
}
