package com.app.CitaMed.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class DiagnosticoDTO {
    @NotNull(message = "Debe existir una cita asociada")
    private Long citaId;

    @NotBlank(message = "Debe indicar la enfermedad")
    @Size(max = 100, message = "El nombre de la enfermedad no puede superar los 100 caracteres")
    private String enfermedad;

    @Size(max = 500, message = "La descripción no puede superar los 500 caracteres")
    private String descripcion;

    @Size(max = 500, message = "La receta no puede superar los 500 caracteres")
    private String receta;

    @Size(max = 500, message = "Las indicaciones no pueden superar los 500 caracteres")
    private String indicaciones;
}
