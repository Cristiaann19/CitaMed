package com.app.CitaMed.DTO;
import com.app.CitaMed.Enums.MetodoPago;
import lombok.Data;

@Data
public class PagoDTO {
    private Long citaId;
    private double monto;
    private MetodoPago metodoPago;
}
