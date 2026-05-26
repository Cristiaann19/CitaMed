import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { CitaDetalle, CitaDTO } from '../../model/Cita';
import { Medico } from '../../model/Medico';
import { Paciente } from '../../model/Paciente';
import { Consultorio } from '../../model/Consultorio';

@Injectable({ providedIn: 'root' })
export class CitaService {
  private apiUrl = 'http://localhost:8080/api';

  constructor(private http: HttpClient) {}

  obtenerTodas(): Observable<CitaDetalle[]> {
    return this.http.get<CitaDetalle[]>(`${this.apiUrl}/cita`);
  }

  obtenerPorMedico(medicoId: number): Observable<CitaDetalle[]> {
    return this.http.get<CitaDetalle[]>(`${this.apiUrl}/cita/medico/${medicoId}`);
  }

  obtenerPorPaciente(pacienteId: number): Observable<CitaDetalle[]> {
    return this.http.get<CitaDetalle[]>(`${this.apiUrl}/cita/paciente/${pacienteId}`);
  }

  registrar(dto: CitaDTO): Observable<string> {
    return this.http.post(`${this.apiUrl}/cita`, dto, { responseType: 'text' });
  }

  actualizar(id: number, dto: Partial<CitaDTO>): Observable<string> {
    return this.http.put(`${this.apiUrl}/cita/${id}`, dto, { responseType: 'text' });
  }

  reprogramar(id: number, nuevaFecha: string): Observable<string> {
    return this.http.patch(
      `${this.apiUrl}/cita/${id}/reprogramar?nuevaFecha=${encodeURIComponent(nuevaFecha)}`,
      {},
      { responseType: 'text' }
    );
  }

  cancelar(id: number): Observable<string> {
    return this.http.patch(`${this.apiUrl}/cita/${id}/cancelar`, {}, { responseType: 'text' });
  }

  completar(id: number): Observable<string> {
    return this.http.patch(`${this.apiUrl}/cita/${id}/completar`, {}, { responseType: 'text' });
  }

  noAsistio(id: number): Observable<string> {
    return this.http.patch(`${this.apiUrl}/cita/${id}/no-asistio`, {}, { responseType: 'text' });
  }

  obtenerMedicos(): Observable<Medico[]> {
    return this.http.get<Medico[]>(`${this.apiUrl}/medico`);
  }

  obtenerMedicosPorEspecialidad(especialidadId: number): Observable<Medico[]> {
    return this.http.get<Medico[]>(`${this.apiUrl}/medico/especialidad/${especialidadId}`);
  }

  obtenerPacientes(): Observable<Paciente[]> {
    return this.http.get<Paciente[]>(`${this.apiUrl}/paciente`);
  }

  obtenerConsultoriosDisponibles(): Observable<Consultorio[]> {
    return this.http.get<Consultorio[]>(`${this.apiUrl}/consultorio/disponibles`);
  }
}
