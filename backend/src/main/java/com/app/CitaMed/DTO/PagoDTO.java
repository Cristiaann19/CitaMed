package com.app.CitaMed.DTO;

import com.app.CitaMed.Enums.MetodoPago;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class PagoDTO {
    @NotNull(message = "La cita es obligatoria")
    private Long citaId;

    @Positive(message = "El monto debe ser mayor que 0")
    private double monto;

    @NotNull(message = "Debe seleccionar un método de pago")
    private MetodoPago metodoPago;
}
