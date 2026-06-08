import { Component, OnInit, ChangeDetectorRef, ChangeDetectionStrategy, ViewChild } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Table, TableModule } from 'primeng/table';
import { DialogModule } from 'primeng/dialog';
import { ButtonModule } from 'primeng/button';
import { InputTextModule } from 'primeng/inputtext';
import { SelectModule } from 'primeng/select';
import { PasswordModule } from 'primeng/password';
import { SortEvent } from 'primeng/api';
import { Usuario, UsuarioDTO, Rol } from '../../../model/Usuario';
import { UsuarioService } from '../../../core/services/usuario-service';
import { GlobalToast } from '../../../core/services/global-toast';

@Component({
  selector: 'app-usuario-component',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    TableModule,
    DialogModule,
    ButtonModule,
    InputTextModule,
    SelectModule,
    PasswordModule,
  ],
  templateUrl: './usuario-component.html',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class UsuarioComponent implements OnInit {
  @ViewChild('dt') dt!: Table;
  initialValue: Usuario[] = [];
  isSorted: boolean | null = null;
  private resetting = false;

  usuarios: Usuario[] = [];
  usuariosFiltrados: Usuario[] = [];
  cargando = true;
  error = '';

  readonly Rol = Rol;

  terminoBusqueda = '';

  mostrarModal = false;
  usuarioForm: UsuarioDTO = { userName: '', password: '', rol: Rol.RECEPCIONISTA };

  mostrarModalRol = false;
  usuarioRolEditando: Usuario | null = null;
  nuevoRol: Rol | null = null;

  mostrarConfirmarEstado = false;
  usuarioCambiandoEstado: Usuario | null = null;

  constructor(
    private usuarioService: UsuarioService,
    private toast: GlobalToast,
    private cdr: ChangeDetectorRef,
  ) {}

  ngOnInit(): void {
    this.cargarUsuarios();
  }

  cargarUsuarios(): void {
    this.cargando = true;
    this.usuarioService.obtenerTodas().subscribe({
      next: (data) => {
        this.usuarios = data;
        this.aplicarFiltros();
        this.cargando = false;
        this.cdr.markForCheck();
      },
      error: () => {
        this.cargando = false;
        this.error = 'Error al cargar los usuarios';
        this.cdr.markForCheck();
      },
    });
  }

  aplicarFiltros(): void {
    let res = [...this.usuarios];

    if (this.terminoBusqueda.trim()) {
      const t = this.terminoBusqueda.toLowerCase();
      res = res.filter((u) => u.userName.toLowerCase().includes(t));
    }

    this.usuariosFiltrados = res;
    this.initialValue = [...res];
    this.isSorted = null;
    this.cdr.markForCheck();
  }

  limpiarFiltros(): void {
    this.terminoBusqueda = '';
    this.aplicarFiltros();
  }

  customSort(event: SortEvent): void {
    if (this.resetting) return;

    if (this.isSorted == null) {
      this.isSorted = true;
      this.sortTableData(event);
    } else if (this.isSorted === true) {
      this.isSorted = false;
      this.sortTableData(event);
    } else {
      this.isSorted = null;
      this.resetting = true;
      this.usuariosFiltrados = [...this.initialValue];
      this.dt.reset();
      setTimeout(() => {
        this.resetting = false;
      }, 0);
    }
  }

  private sortTableData(event: SortEvent): void {
    this.usuariosFiltrados.sort((data1, data2) => {
      const value1 = (data1 as any)[event.field!];
      const value2 = (data2 as any)[event.field!];
      let result: number;

      if (value1 == null && value2 != null) result = -1;
      else if (value1 != null && value2 == null) result = 1;
      else if (value1 == null && value2 == null) result = 0;
      else if (typeof value1 === 'string' && typeof value2 === 'string')
        result = value1.localeCompare(value2);
      else if (typeof value1 === 'boolean' && typeof value2 === 'boolean')
        result = value1 === value2 ? 0 : value1 ? 1 : -1;
      else result = value1 < value2 ? -1 : value1 > value2 ? 1 : 0;

      return event.order! * result;
    });
  }

  abrirNueva(): void {
    this.usuarioForm = { userName: '', password: '', rol: Rol.RECEPCIONISTA };
    this.mostrarModal = true;
  }

  guardar(): void {
    if (!this.usuarioForm.userName.trim()) {
      this.toast.warn('El nombre de usuario es obligatorio');
      return;
    }
    if (!this.usuarioForm.password || this.usuarioForm.password.length < 6) {
      this.toast.warn('La contraseña debe tener al menos 6 caracteres');
      return;
    }

    this.usuarioService.crear(this.usuarioForm).subscribe({
      next: (res) => {
        this.toast.success(res);
        this.mostrarModal = false;
        this.cargarUsuarios();
      },
      error: (err) => this.toast.error(err?.error || 'Error al registrar el usuario'),
    });
  }

  abrirCambiarRol(usuario: Usuario): void {
    this.usuarioRolEditando = usuario;
    this.nuevoRol = usuario.rol;
    this.mostrarModalRol = true;
  }

  cambiarRol(): void {
    if (!this.usuarioRolEditando || this.nuevoRol == null) return;
    if (this.nuevoRol === this.usuarioRolEditando.rol) {
      this.mostrarModalRol = false;
      return;
    }

    this.usuarioService.cambiarRol(this.usuarioRolEditando.id!, this.nuevoRol).subscribe({
      next: (res) => {
        this.toast.success(res);
        this.mostrarModalRol = false;
        this.usuarioRolEditando = null;
        this.cargarUsuarios();
      },
      error: (err) => this.toast.error(err?.error || 'Error al cambiar el rol'),
    });
  }

  confirmarCambiarEstado(usuario: Usuario): void {
    this.usuarioCambiandoEstado = usuario;
    this.mostrarConfirmarEstado = true;
  }

  toggleEstado(): void {
    if (!this.usuarioCambiandoEstado) return;

    this.usuarioService.cambiarEstado(this.usuarioCambiandoEstado.id!).subscribe({
      next: (res) => {
        this.toast.success(res);
        this.mostrarConfirmarEstado = false;
        this.usuarioCambiandoEstado = null;
        this.cargarUsuarios();
      },
      error: (err) => this.toast.error(err?.error || 'Error al cambiar el estado'),
    });
  }

  obtenerClaseRol(rol: Rol): string {
    const map: Record<Rol, string> = {
      [Rol.ADMIN]: 'bg-red-100 text-red-800',
      [Rol.MEDICO]: 'bg-blue-100 text-blue-800',
      [Rol.RECEPCIONISTA]: 'bg-amber-100 text-amber-800',
    };
    return map[rol] ?? '';
  }
}
