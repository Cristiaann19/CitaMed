import {
  ChangeDetectionStrategy,
  ChangeDetectorRef,
  Component,
  OnInit,
} from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { DialogModule } from 'primeng/dialog';
import { ButtonModule } from 'primeng/button';
import { TableModule } from 'primeng/table';
import { DatePickerModule } from 'primeng/datepicker';
import { SelectModule } from 'primeng/select';
import { InputTextModule } from 'primeng/inputtext';
import { TextareaModule } from 'primeng/textarea';
import { CitaService } from '../../../core/services/cita-service';
import { CitaDetalle, CitaDTO, EstadoCita } from '../../../model/Cita';
import { Medico } from '../../../model/Medico';
import { Paciente } from '../../../model/Paciente';
import { Consultorio } from '../../../model/Consultorio';
import { Especialidad } from '../../../model/Especialidad';
import { GlobalToast } from '../../../core/services/global-toast';
import { HttpClient } from '@angular/common/http';

interface FiltroEstado {
  label: string;
  value: EstadoCita | null;
}

@Component({
  selector: 'app-citas-component',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    DialogModule,
    ButtonModule,
    TableModule,
    DatePickerModule,
    SelectModule,
    InputTextModule,
    TextareaModule,
  ],
  templateUrl: './citas-component.html',
  styleUrl: './citas-component.css',
  changeDetection: ChangeDetectionStrategy.OnPush,
})

export class CitasComponent implements OnInit {
  citas: CitaDetalle[] = [];
  citasFiltradas: CitaDetalle[] = [];
  medicos: Medico[] = [];
  medicosFiltrados: Medico[] = [];
  pacientes: Paciente[] = [];
  consultorios: Consultorio[] = [];
  especialidades: Especialidad[] = [];

  readonly hoy: Date = new Date();
  readonly EstadoCita = EstadoCita;

  mostrarModal = false;
  mostrarDetalleModal = false;
  mostrarReprogramarModal = false;
  modoEdicion = false;

  citaForm: CitaDTO = this.resetCitaForm();
  fechaHoraCita: Date | null = null;
  especialidadSeleccionada: number | null = null;

  citaEditandoId: number | null = null;

  citaSeleccionada: CitaDetalle | null = null;
  nuevaFechaReprogramacion: Date | null = null;

  terminoBusqueda = '';
  filtroEstado: EstadoCita | null = null;
  filtroMedico: number | null = null;
  filtroFechaInicio: Date | null = null;
  filtroFechaFin: Date | null = null;

  estadosDisponibles: FiltroEstado[] = [
    { label: 'Todas',      value: null },
    { label: 'Programada', value: EstadoCita.PROGRAMADA },
    { label: 'Atendida',   value: EstadoCita.ATENDIDA },
    { label: 'Cancelada',  value: EstadoCita.CANCELADA },
    { label: 'No Asistió', value: EstadoCita.NO_ASISTIO },
  ];

  constructor(
    private citaService: CitaService,
    private toast: GlobalToast,
    private cdr: ChangeDetectorRef,
    private http: HttpClient,
  ) {}

  ngOnInit(): void {
    this.cargarCitas();
    this.cargarMedicos();
    this.cargarPacientes();
    this.cargarConsultorios();
    this.cargarEspecialidades();
  }

  cargarCitas(): void {
    this.citaService.obtenerTodas().subscribe({
      next: (data) => {
        this.citas = data;
        this.aplicarFiltros();
        this.cdr.markForCheck();
      },
      error: () => this.toast.error('No se pudieron cargar las citas'),
    });
  }

  cargarMedicos(): void {
    this.citaService.obtenerMedicos().subscribe({
      next: (data) => {
        this.medicos = data.filter((m) => m.activo);
        this.medicosFiltrados = [...this.medicos];
        this.cdr.markForCheck();
      },
    });
  }

  cargarPacientes(): void {
    this.citaService.obtenerPacientes().subscribe({
      next: (data) => {
        this.pacientes = Array.isArray(data)
          ? (data as any[]).filter((p) => p.activo !== false)
          : (data as any).content ?? [];
        this.cdr.markForCheck();
      },
    });
  }

  cargarConsultorios(): void {
    this.citaService.obtenerConsultoriosDisponibles().subscribe({
      next: (data) => { this.consultorios = data; this.cdr.markForCheck(); },
    });
  }

  cargarEspecialidades(): void {
    this.http.get<Especialidad[]>('http://localhost:8080/api/especialidad').subscribe({
      next: (data) => { this.especialidades = data; this.cdr.markForCheck(); },
    });
  }

  aplicarFiltros(): void {
    let res = [...this.citas];

    if (this.terminoBusqueda.trim()) {
      const t = this.terminoBusqueda.toLowerCase();
      res = res.filter((c) => {
        const pac = `${c.pacienteNombre} ${c.pacienteApellidoPaterno} ${c.pacienteApellidoMaterno}`.toLowerCase();
        const med = `${c.medicoNombre} ${c.medicoApellidoPaterno} ${c.medicoApellidoMaterno}`.toLowerCase();
        return pac.includes(t) || med.includes(t) || c.pacienteDni.includes(t);
      });
    }

    if (this.filtroEstado) res = res.filter((c) => c.estado === this.filtroEstado);
    if (this.filtroMedico)  res = res.filter((c) => c.medicoId === this.filtroMedico);

    if (this.filtroFechaInicio)
      res = res.filter((c) => new Date(c.fechaHora) >= this.filtroFechaInicio!);

    if (this.filtroFechaFin) {
      const fin = new Date(this.filtroFechaFin);
      fin.setHours(23, 59, 59, 999);
      res = res.filter((c) => new Date(c.fechaHora) <= fin);
    }

    this.citasFiltradas = res;
    this.cdr.markForCheck();
  }

  limpiarFiltros(): void {
    this.terminoBusqueda = '';
    this.filtroEstado = null;
    this.filtroMedico = null;
    this.filtroFechaInicio = null;
    this.filtroFechaFin = null;
    this.aplicarFiltros();
  }

  abrirNuevaCita(): void {
    this.modoEdicion = false;
    this.citaEditandoId = null;
    this.citaForm = this.resetCitaForm();
    this.fechaHoraCita = null;
    this.especialidadSeleccionada = null;
    this.medicosFiltrados = [...this.medicos];
    this.mostrarModal = true;
  }

  abrirEditarCita(cita: CitaDetalle): void {
    this.modoEdicion = true;
    this.citaEditandoId = cita.id;
    this.especialidadSeleccionada = null;
    this.medicosFiltrados = [...this.medicos];

    this.citaForm = {
      pacienteId: cita.pacienteId,
      medicoId:   cita.medicoId,
      consultorioId: cita.consultorioId,
      fechaHora:  cita.fechaHora,
      motivoConsulta: cita.motivoConsulta,
    };
    this.fechaHoraCita = new Date(cita.fechaHora);
    this.mostrarModal = true;
  }

  verDetalle(cita: CitaDetalle): void {
    this.citaSeleccionada = cita;
    this.mostrarDetalleModal = true;
  }

  abrirReprogramar(cita: CitaDetalle): void {
    this.citaSeleccionada = cita;
    this.nuevaFechaReprogramacion = new Date(cita.fechaHora);
    this.mostrarReprogramarModal = true;
  }

  onEspecialidadChange(): void {
    this.citaForm.medicoId = 0;
    if (!this.especialidadSeleccionada) {
      this.medicosFiltrados = [...this.medicos];
    } else {
      this.citaService.obtenerMedicosPorEspecialidad(this.especialidadSeleccionada).subscribe({
        next: (data) => {
          this.medicosFiltrados = data.filter((m) => m.activo);
          this.cdr.markForCheck();
        },
      });
    }
  }

  guardarCita(): void {
    if (!this.citaForm.pacienteId)   { this.toast.warn('Seleccione un paciente');    return; }
    if (!this.citaForm.medicoId)     { this.toast.warn('Seleccione un médico');      return; }
    if (!this.citaForm.consultorioId){ this.toast.warn('Seleccione un consultorio'); return; }
    if (!this.fechaHoraCita)         { this.toast.warn('Seleccione fecha y hora');   return; }
    if (!this.citaForm.motivoConsulta.trim()) { this.toast.warn('Ingrese el motivo'); return; }

    this.citaForm.fechaHora = this.fechaHoraCita.toISOString();

    if (this.modoEdicion && this.citaEditandoId) {
      this.citaService.actualizar(this.citaEditandoId, this.citaForm).subscribe({
        next: (res) => { this.toast.success(res); this.cerrarModalYRecargar(); },
        error: (err) => this.toast.error(err.error || 'Error al actualizar la cita'),
      });
    } else {
      this.citaService.registrar(this.citaForm).subscribe({
        next: (res) => { this.toast.success(res); this.cerrarModalYRecargar(); },
        error: (err) => this.toast.error(err.error || 'Error al registrar la cita'),
      });
    }
  }

  reprogramarCita(): void {
    if (!this.citaSeleccionada || !this.nuevaFechaReprogramacion) {
      this.toast.warn('Seleccione una nueva fecha y hora');
      return;
    }
    this.citaService.reprogramar(this.citaSeleccionada.id, this.nuevaFechaReprogramacion.toISOString()).subscribe({
      next: (res) => { this.toast.success(res); this.mostrarReprogramarModal = false; this.cargarCitas(); },
      error: (err) => this.toast.error(err.error || 'Error al reprogramar'),
    });
  }

  cancelarCita(cita: CitaDetalle): void {
    if (!confirm(`¿Cancelar la cita de ${cita.pacienteNombre} ${cita.pacienteApellidoPaterno}?`)) return;
    this.citaService.cancelar(cita.id).subscribe({
      next: (res) => { this.toast.success(res); this.cargarCitas(); },
      error: (err) => this.toast.error(err.error || 'Error al cancelar'),
    });
  }

  completarCita(cita: CitaDetalle): void {
    this.citaService.completar(cita.id).subscribe({
      next: (res) => { this.toast.success(res); this.cargarCitas(); },
      error: (err) => this.toast.error(err.error || 'Error al completar'),
    });
  }

  marcarNoAsistio(cita: CitaDetalle): void {
    this.citaService.noAsistio(cita.id).subscribe({
      next: (res) => { this.toast.success(res); this.cargarCitas(); },
      error: (err) => this.toast.error(err.error || 'Error al actualizar'),
    });
  }

  private cerrarModalYRecargar(): void {
    this.mostrarModal = false;
    this.cargarCitas();
  }

  private resetCitaForm(): CitaDTO {
    return { pacienteId: 0, medicoId: 0, consultorioId: 0, fechaHora: '', motivoConsulta: '' };
  }

  nombreCompleto(nombre: string, ap: string, am: string): string {
    return `${nombre} ${ap} ${am}`;
  }

  obtenerClaseEstado(estado: EstadoCita): string {
    const map: Record<EstadoCita, string> = {
      [EstadoCita.PROGRAMADA]: 'bg-teal-light text-teal-dark',
      [EstadoCita.ATENDIDA]:   'bg-green-bg text-green-text',
      [EstadoCita.CANCELADA]:  'bg-red-bg text-red-text',
      [EstadoCita.NO_ASISTIO]: 'bg-amber-bg text-amber-text',
    };
    return map[estado] ?? '';
  }

  puedeReprogramar  = (c: CitaDetalle) => c.estado === EstadoCita.PROGRAMADA;
  puedeCancelar     = (c: CitaDetalle) => c.estado === EstadoCita.PROGRAMADA;
  puedeCompletar    = (c: CitaDetalle) => c.estado === EstadoCita.PROGRAMADA;
  puedeMarcarNoAsistio = (c: CitaDetalle) => c.estado === EstadoCita.PROGRAMADA;
  puedeEditar       = (c: CitaDetalle) => c.estado === EstadoCita.PROGRAMADA;
}
