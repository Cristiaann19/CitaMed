import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
export interface HorarioMedico {
  id: number;
  dia: DiaSemana;
  horaInicio: string;
  horaFin: string;
  activo: boolean;
}

export interface HorarioMedicoDTO {
  medicoId: number;
  consultorioId: number;
  dia: DiaSemana;
  horaInicio: string;
  horaFin: string;
}

export type DiaSemana =
  | 'LUNES' | 'MARTES' | 'MIERCOLES' | 'JUEVES'
  | 'VIERNES' | 'SABADO' | 'DOMINGO';

export interface Medico {
  id: number;
  nombre: string;
  apellidoPaterno: string;
  apellidoMaterno: string;
  numeroDocumento: string;
  numeroColegiatura: string;
  activo: boolean;
  especialidad: { id: number; nombre: string };
}

export interface Consultorio {
  id: number;
  nombre: string;
  numero: string;
  disponible: boolean;
}

@Injectable({ providedIn: 'root' })
export class HorarioMedicoService {
  private http = inject(HttpClient);
  private base = 'http://localhost:8080/api';

  // ── MÉDICOS ──────────────────────────────────────────
  getMedicos(): Observable<Medico[]> {
    return this.http.get<Medico[]>(`${this.base}/medico`);
  }

  // ── CONSULTORIOS ─────────────────────────────────────
  getConsultorios(): Observable<Consultorio[]> {
    return this.http.get<Consultorio[]>(`${this.base}/consultorio`);
  }

  // ── HORARIOS ─────────────────────────────────────────
  getHorariosPorMedico(medicoId: number): Observable<HorarioMedico[]> {
    return this.http.get<HorarioMedico[]>(`${this.base}/horarioMedico/medico/${medicoId}`);
  }

  crearHorario(dto: HorarioMedicoDTO): Observable<string> {
    return this.http.post(`${this.base}/horarioMedico`, dto, { responseType: 'text' });
  }

  toggleActivo(id: number): Observable<string> {
    return this.http.patch(`${this.base}/horarioMedico/${id}/estado`, {}, { responseType: 'text' });
  }
}