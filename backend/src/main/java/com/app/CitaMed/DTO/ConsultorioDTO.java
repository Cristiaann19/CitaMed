package com.app.CitaMed.DTO;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ConsultorioDTO {
    @NotBlank(message = "El número del consultorio es obligatorio")
    private String numero;

    @NotBlank(message = "La descripción es obligatoria")
    private String descripcion;
}
