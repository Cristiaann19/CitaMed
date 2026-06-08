import { Component, OnInit, ChangeDetectorRef, ChangeDetectionStrategy, ViewChild } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Table, TableModule } from 'primeng/table';
import { DialogModule } from 'primeng/dialog';
import { ButtonModule } from 'primeng/button';
import { InputTextModule } from 'primeng/inputtext';
import { TextareaModule } from 'primeng/textarea';
import { SortEvent } from 'primeng/api';
import { Especialidad } from '../../../model/Especialidad';
import { EspecialidadService } from '../../../core/services/especialidad-service';
import { GlobalToast } from '../../../core/services/global-toast';

@Component({
  selector: 'app-especialidades-component',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    TableModule,
    DialogModule,
    ButtonModule,
    InputTextModule,
    TextareaModule,
  ],
  templateUrl: './especialidades-component.html',
  styleUrl: './especialidades-component.css',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class EspecialidadesComponent implements OnInit {
  @ViewChild('dt') dt!: Table;
  initialValue: Especialidad[] = [];
  isSorted: boolean | null = null;
  private resetting = false;

  especialidades: Especialidad[] = [];
  especialidadesFiltradas: Especialidad[] = [];
  cargando = true;
  error = '';

  terminoBusqueda = '';

  mostrarModal = false;
  modoEdicion = false;
  especialidadEditandoId: number | null = null;
  especialidadForm: { nombre: string; descripcion: string } = { nombre: '', descripcion: '' };

  mostrarConfirmarDelete = false;
  especialidadEliminando: Especialidad | null = null;

  constructor(
    private especialidadService: EspecialidadService,
    private toast: GlobalToast,
    private cdr: ChangeDetectorRef,
  ) {}

  ngOnInit(): void {
    this.cargarEspecialidades();
  }

  cargarEspecialidades(): void {
    this.cargando = true;
    this.especialidadService.obtenerTodas().subscribe({
      next: (data) => {
        this.especialidades = data;
        this.aplicarFiltros();
        this.cargando = false;
        this.cdr.markForCheck();
      },
      error: () => {
        this.cargando = false;
        this.error = 'Error al cargar las especialidades';
        this.cdr.markForCheck();
      },
    });
  }

  aplicarFiltros(): void {
    let res = [...this.especialidades];

    if (this.terminoBusqueda.trim()) {
      const t = this.terminoBusqueda.toLowerCase();
      res = res.filter(
        (e) =>
          e.nombre.toLowerCase().includes(t) ||
          (e.descripcion || '').toLowerCase().includes(t),
      );
    }

    this.especialidadesFiltradas = res;
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
      this.especialidadesFiltradas = [...this.initialValue];
      this.dt.reset();
      setTimeout(() => {
        this.resetting = false;
      }, 0);
    }
  }

  private sortTableData(event: SortEvent): void {
    this.especialidadesFiltradas.sort((data1, data2) => {
      const value1 = (data1 as any)[event.field!];
      const value2 = (data2 as any)[event.field!];
      let result: number;

      if (value1 == null && value2 != null) result = -1;
      else if (value1 != null && value2 == null) result = 1;
      else if (value1 == null && value2 == null) result = 0;
      else if (typeof value1 === 'string' && typeof value2 === 'string')
        result = value1.localeCompare(value2);
      else result = value1 < value2 ? -1 : value1 > value2 ? 1 : 0;

      return event.order! * result;
    });
  }

  abrirNueva(): void {
    this.modoEdicion = false;
    this.especialidadEditandoId = null;
    this.especialidadForm = { nombre: '', descripcion: '' };
    this.mostrarModal = true;
  }

  abrirEditar(esp: Especialidad): void {
    this.modoEdicion = true;
    this.especialidadEditandoId = esp.id;
    this.especialidadForm = { nombre: esp.nombre, descripcion: esp.descripcion };
    this.mostrarModal = true;
  }

  guardar(): void {
    if (!this.especialidadForm.nombre.trim()) {
      this.toast.warn('El nombre de la especialidad es obligatorio');
      return;
    }

    if (this.modoEdicion && this.especialidadEditandoId) {
      this.especialidadService.actualizar(this.especialidadEditandoId, this.especialidadForm).subscribe({
        next: (res) => {
          this.toast.success(res);
          this.mostrarModal = false;
          this.cargarEspecialidades();
        },
        error: (err) => this.toast.error(err?.error?.mensaje || 'Error al actualizar la especialidad'),
      });
    } else {
      this.especialidadService.crear(this.especialidadForm).subscribe({
        next: (res) => {
          this.toast.success(res);
          this.mostrarModal = false;
          this.cargarEspecialidades();
        },
        error: (err) => this.toast.error(err?.error?.mensaje || 'Error al registrar la especialidad'),
      });
    }
  }

  confirmarEliminar(esp: Especialidad): void {
    this.especialidadEliminando = esp;
    this.mostrarConfirmarDelete = true;
  }

  eliminar(): void {
    if (!this.especialidadEliminando) return;
    this.especialidadService.eliminar(this.especialidadEliminando.id).subscribe({
      next: (res) => {
        this.toast.success(res);
        this.mostrarConfirmarDelete = false;
        this.especialidadEliminando = null;
        this.cargarEspecialidades();
      },
      error: (err) => this.toast.error(err?.error?.mensaje || 'Error al eliminar la especialidad'),
    });
  }
}
