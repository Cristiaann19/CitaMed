package com.app.CitaMed.DTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class RecetaDTO {
    public String paciente;
    public String dni;
    public String edad;
    public String medico;
    public String especialidad;
    public String diagnostico;
    public String receta;
    public String indicaciones;
    public String fecha;
}
