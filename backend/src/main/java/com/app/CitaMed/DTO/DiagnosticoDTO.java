package com.app.CitaMed.DTO;
import lombok.Data;

@Data
public class DiagnosticoDTO {
    private Long citaId;
    private String enfermedad;
    private String descripcion;
    private String receta;
    private String indicaciones;
}
