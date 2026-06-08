import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Usuario, UsuarioDTO, Rol } from '../../model/Usuario';

@Injectable({ providedIn: 'root' })
export class UsuarioService {
  private apiUrl = 'http://localhost:8080/api/usuario';

  constructor(private http: HttpClient) {}

  obtenerTodas(): Observable<Usuario[]> {
    return this.http.get<Usuario[]>(this.apiUrl);
  }

  crear(data: UsuarioDTO): Observable<string> {
    return this.http.post(this.apiUrl, data, { responseType: 'text' });
  }

  cambiarEstado(id: number): Observable<string> {
    return this.http.patch(`${this.apiUrl}/${id}/estado`, {}, { responseType: 'text' });
  }

  cambiarRol(id: number, nuevoRol: Rol): Observable<string> {
    return this.http.patch(`${this.apiUrl}/${id}/rol`, null, {
      params: { nuevoRol },
      responseType: 'text',
    });
  }
}
