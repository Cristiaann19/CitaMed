import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { Observable, tap } from 'rxjs';
import { LoginResponse } from '../../model/Perfil';

@Injectable({ providedIn: 'root' })
export class AuthService {
  private apiUrl = 'http://localhost:8080/api/auth';

  constructor(
    private http: HttpClient,
    private router: Router,
  ) {}

  login(usuario: string, clave: string): Observable<LoginResponse> {
    return this.http.post<LoginResponse>(`${this.apiUrl}/login`, { usuario, clave }).pipe(
      tap((response) => {
        localStorage.setItem('token', response.token);
        localStorage.setItem('nombreUsuario', response.nombre);
        localStorage.setItem('usuario', response.usuario);
        localStorage.setItem('rol', response.perfil);
      }),
    );
  }

  getToken(): string | null {
    return localStorage.getItem('token');
  }

  getNombre(): string | null {
    return localStorage.getItem('nombreUsuario');
  }

  getPerfil(): string | null {
    return localStorage.getItem('rol');
  }

  isLoggedIn(): boolean {
    return !!this.getToken();
  }

  logout(): void {
    localStorage.clear();
    this.router.navigate(['/login']);
  }

  // --- JWT helpers ---
  private parseJwt(token: string): any | null {
    try {
      const payload = token.split('.')[1];
      // base64url to base64
      const base64 = payload.replace(/-/g, '+').replace(/_/g, '/');
      const decoded = atob(base64);
      // decodeURIComponent/escape to handle UTF-8
      return JSON.parse(decodeURIComponent(escape(decoded)));
    } catch (e) {
      return null;
    }
  }

  getRolesFromToken(): string[] {
    const token = this.getToken();
    if (!token) return [];
    const payload = this.parseJwt(token);
    if (!payload) return [];
    const roles = payload['roles'];
    if (Array.isArray(roles)) return roles;
    const perfil = this.getPerfil();
    return perfil ? [perfil] : [];
  }

  getPrimaryRole(): string | null {
    const roles = this.getRolesFromToken();
    return roles.length ? roles[0] : null;
  }

  isAdmin(): boolean {
    return this.getRolesFromToken().some((r) => r === 'ADMIN' || r === 'ROLE_ADMIN');
  }

  isMedico(): boolean {
    return this.getRolesFromToken().some((r) => r === 'MEDICO' || r === 'ROLE_MEDICO');
  }

  isRecepcionista(): boolean {
    return this.getRolesFromToken().some((r) => r === 'RECEPCIONISTA' || r === 'ROLE_RECEPCIONISTA');
  }
}
