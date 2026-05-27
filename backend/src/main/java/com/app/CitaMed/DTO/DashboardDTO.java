package com.app.CitaMed.DTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class DashboardDTO {
    private Long citasHoy;
    private Long citasAyer;
    private Long pacientesActivos;
    private Long pacientesMesAnterior;
    private Double ingresoMes;
    private Double ingresoMesAnterior;
    private Long canceladasSemana;
    private Long canceladasSemanaAnterior;
    private Long totalPacientes;
    private Long totalMedicos;
    private Long totalCitas;
}
