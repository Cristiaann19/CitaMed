import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HttpClient } from '@angular/common/http';
import { RouterLink } from '@angular/router';

interface DashboardDTO {
  citasHoy: number;
  citasAyer: number;
  pacientesActivos: number;
  pacientesMesAnterior: number;
  ingresoMes: number;
  ingresoMesAnterior: number;
  canceladasSemana: number;
  canceladasSemanaAnterior: number;
}

interface UltimaCitaDTO {
  paciente: string;
  medico: string;
  especialidad: string;
  fecha: string;
  estado: string;
}

interface EspecialidadDTO {
  especialidad: string;
  total: number;
}

interface PagoDTO {
  paciente: string;
  metodo: string;
  citaId: number;
  monto: number;
  estado: string;
}

interface AgendaHoyDTO {
  hora: string;
  paciente: string;
  detalle: string;
}

interface MedicoActivoDTO {
  iniciales: string;
  medico: string;
  cmp: string;
  especialidad: string;
  citas: number;
}

@Component({
  selector: 'app-dashboard-component',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './dashboard-component.html',
  styleUrl: './dashboard-component.css',
})

export class DashboardComponent implements OnInit {

  private http = inject(HttpClient);

  medicos: MedicoActivoDTO[] = [];
  hoy: Date = new Date();
  agendaHoy: AgendaHoyDTO[] = [];
  especialidades: EspecialidadDTO[] = [];
  stats: DashboardDTO = {
    citasHoy: 0,
    citasAyer: 0,
    pacientesActivos: 0,
    pacientesMesAnterior: 0,
    ingresoMes: 0,
    ingresoMesAnterior: 0,
    canceladasSemana: 0,
    canceladasSemanaAnterior: 0
  };

  ultimasCitas: UltimaCitaDTO[] = [];
  pagos: PagoDTO[] = [];

  ngOnInit(): void {
    this.cargarDashboard();
    this.cargarUltimasCitas();
    this.cargarEspecialidades();
    this.cargarMedicos();
    this.cargarAgendaHoy();
    this.cargarPagos();
  }

  cargarMedicos() {
    this.http
      .get<MedicoActivoDTO[]>('http://localhost:8080/api/dashboard/medicos')
      .subscribe({
        next: (data) => (this.medicos = data),
        error: (err) => console.log(err)
      });
  }

  cargarDashboard(): void {
    this.http.get<DashboardDTO>('http://localhost:8080/api/dashboard')
      .subscribe({
        next: (data) => {
          this.stats = data;

          this.statsDiff = {
            citas: data.citasHoy - data.citasAyer,
            pacientes: data.pacientesActivos - data.pacientesMesAnterior,
            ingresos: data.ingresoMes - data.ingresoMesAnterior,
            canceladas: data.canceladasSemana - data.canceladasSemanaAnterior
          };

          this.statsTrend = {
            citasUp: this.statsDiff.citas >= 0,
            pacientesUp: this.statsDiff.pacientes >= 0,
            ingresosUp: this.statsDiff.ingresos >= 0,
            canceladasUp: this.statsDiff.canceladas >= 0
          };

          this.ingresosPorcentaje =
            this.porcentaje(data.ingresoMes, data.ingresoMesAnterior);
        },
        error: (err) => console.log(err)
      });
  }

  cargarAgendaHoy() {
    this.http
      .get<AgendaHoyDTO[]>('http://localhost:8080/api/dashboard/agenda')
      .subscribe({
        next: (data) => (this.agendaHoy = data),
        error: (err) => console.log(err)
      });
  }

  cargarEspecialidades() {
    this.http
      .get<EspecialidadDTO[]>('http://localhost:8080/api/dashboard/especialidades')
      .subscribe({
        next: (data) => (this.especialidades = data),
        error: (err) => console.log(err)
      });
  }

  cargarUltimasCitas(): void {
    this.http.get<UltimaCitaDTO[]>('http://localhost:8080/api/dashboard/ultimas-citas')
      .subscribe({
        next: (data) => this.ultimasCitas = data,
        error: (err) => console.log(err)
      });
  }

  diferencia(actual: number, anterior: number): number {
    return actual - anterior;
  }

  statsDiff = {
    citas: 0,
    pacientes: 0,
    ingresos: 0,
    canceladas: 0
  };

  statsTrend = {
    citasUp: true,
    pacientesUp: true,
    ingresosUp: true,
    canceladasUp: true
  };

  ingresosPorcentaje = 0;

  porcentaje(actual: number, anterior: number): number {
    if (anterior === 0) return 0;
    return ((actual - anterior) / anterior) * 100;
  }

  cargarPagos(): void {
    this.http.get<any[]>('http://localhost:8080/api/dashboard/pagos')
      .subscribe({
        next: (data) => {
          this.pagos = data;
        },
        error: (err) => console.log(err)
      });
  }
}
