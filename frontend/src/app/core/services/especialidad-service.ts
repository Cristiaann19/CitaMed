import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Especialidad } from '../../model/Especialidad';

@Injectable({ providedIn: 'root' })
export class EspecialidadService {
  private apiUrl = 'http://localhost:8080/api/especialidad';

  constructor(private http: HttpClient) {}

  obtenerTodas(): Observable<Especialidad[]> {
    return this.http.get<Especialidad[]>(this.apiUrl);
  }
}
