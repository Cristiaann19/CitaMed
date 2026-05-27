import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import {
  RouterOutlet,
  Router,
  NavigationEnd,
  ActivatedRoute,
  RouterLink,
  RouterLinkActive,
} from '@angular/router';
import { CommonModule } from '@angular/common';
import { filter, map, mergeMap } from 'rxjs/operators';
import { AuthService } from '../../core/services/auth-service';

@Component({
  selector: 'app-dashboard-layout',
  standalone: true,
  imports: [RouterOutlet, CommonModule, RouterLink, RouterLinkActive],
  templateUrl: './dashboard-layout.html',
})
export class DashboardLayout implements OnInit {
  titulo: string = 'Panel Administrativo';
  nombre: string = '';
  rol: string = '';
  inicial: string = '';

  constructor(
    private router: Router,
    private activatedRoute: ActivatedRoute,
    private cdr: ChangeDetectorRef,
    private authService: AuthService,
  ) {}

  ngOnInit(): void {
    this.router.events
      .pipe(
        filter((event) => event instanceof NavigationEnd),
        map(() => this.activatedRoute),
        map((route) => {
          while (route.firstChild) route = route.firstChild;
          return route;
        }),
        mergeMap((route) => route.data),
      )
      .subscribe((data) => {
        this.titulo = data['title'] || 'Panel Administrativo';
        this.cdr.detectChanges();
      });

    this.actualizarTituloManual();
    this.cargarDatosUsuario();
  }

  private actualizarTituloManual(): void {
    let route = this.activatedRoute.root;
    while (route.firstChild) route = route.firstChild;
    route.data.subscribe((data) => {
      this.titulo = data['title'] || 'Panel Administrativo';
      this.cdr.detectChanges();
    });
  }

  cerrarSesion(): void {
    this.authService.logout();
  }

  private cargarDatosUsuario(): void {
    this.nombre = this.authService.getNombre() || 'Usuario';
    const primary = this.authService.getPrimaryRole();
    this.rol = primary || this.authService.getPerfil() || 'Sin Rol';
    this.inicial = this.nombre ? this.nombre.charAt(0).toUpperCase() : 'U';
    this.cdr.detectChanges();
  }

  isAdminView(): boolean {
    return this.authService.isAdmin();
  }

  isMedicoView(): boolean {
    return this.authService.isMedico();
  }

  isRecepcionistaView():boolean {
    return this.authService.isRecepcionista();
  }
}
