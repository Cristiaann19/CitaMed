package com.app.CitaMed.DTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmailRequestDTO {
    @NotBlank(message = "El destinatario es obligatorio")
    @Email(message = "Debe ingresar un email válido")
    private String destino;

    @NotBlank(message = "El asunto es obligatorio")
    private String asunto;

    private String html;
}
