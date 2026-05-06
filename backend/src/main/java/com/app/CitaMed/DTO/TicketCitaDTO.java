package com.app.CitaMed.DTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class TicketCitaDTO {
    public String cliente;
    public String dni;
    public String fecha;
    public String hora;
    public String numeroCita;
    public String medico;
    public String especialidad;
    public String metodoPago;
    public Double monto;
}
