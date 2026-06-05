import {
  ChangeDetectionStrategy,
  ChangeDetectorRef,
  Component,
  OnInit,
  viewChild,
  ViewChild,
} from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { DialogModule } from 'primeng/dialog';
import { ButtonModule } from 'primeng/button';
import { Table, TableModule } from 'primeng/table';
import { DatePickerModule } from 'primeng/datepicker';
import { SelectModule } from 'primeng/select';
import { InputTextModule } from 'primeng/inputtext';
import { TextareaModule } from 'primeng/textarea';
import { CitaService } from '../../../core/services/cita-service';
import { CitaDetalle, CitaDTO, EstadoCita } from '../../../model/Cita';
import { Medico } from '../../../model/Medico';
import { Paciente } from '../../../model/Paciente';
import { Especialidad } from '../../../model/Especialidad';
import { GlobalToast } from '../../../core/services/global-toast';
import { HttpClient } from '@angular/common/http';
import { SortEvent } from 'primeng/api';
import { HorarioMedicoService, HorarioMedico } from '../../../core/services/horario-medico-service';

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
  @ViewChild('dt') dt!: Table;
  initialValue: CitaDetalle[] = [];
  isSorted: boolean | null = null;

  citas: CitaDetalle[] = [];
  citasFiltradas: CitaDetalle[] = [];
  medicos: Medico[] = [];
  medicosFiltrados: Medico[] = [];
  pacientes: Paciente[] = [];
  especialidades: Especialidad[] = [];

  readonly hoy: Date = new Date();
  readonly EstadoCita = EstadoCita;

  mostrarModal = false;
  mostrarDetalleModal = false;
  mostrarReprogramarModal = false;
  mostrarConfirmarDelete = false;
  mostrarConfirmarCancelar = false;
  modoEdicion = false;

  citaDeleteando: CitaDetalle | null = null;
  citaCancelando: CitaDetalle | null = null;

  nuevaCita: CitaDTO = this.resetCitaForm();
  fechaHoraCita: Date | null = null;
  especialidadSeleccionada: number | null = null;
  horariosMedico: HorarioMedico[] = [];

  citaEditandoId: number | null = null;

  citaSeleccionada: CitaDetalle | null = null;
  nuevaFechaReprogramacion: Date | null = null;

  terminoBusqueda = '';
  filtroEstado: EstadoCita | null = null;
  filtroMedico: number | null = null;
  filtroFechaInicio: Date | null = null;
  filtroFechaFin: Date | null = null;

  // DNI search state
  dniPaciente = '';
  dniTouched = false;
  buscandoDni = false;
  pacienteEncontrado: Paciente | null = null;
  pacienteNoEncontrado = false;
  mostrarCamposPaciente = false;
  reniecCargando = false;

  // New patient form
  nuevoPaciente = {
    nombre: '',
    apellidoPaterno: '',
    apellidoMaterno: '',
    telefono: '',
    email: '',
    direccion: '',
    fechaNacimiento: '',
    genero: '',
    grupoSanguineo: '',
  };

  generos = [
    { label: 'Masculino', value: 'MASCULINO' },
    { label: 'Femenino', value: 'FEMENINO' },
  ];

  gruposSanguineos = [
    { label: 'A+', value: 'A_POSITIVO' },
    { label: 'A-', value: 'A_NEGATIVO' },
    { label: 'B+', value: 'B_POSITIVO' },
    { label: 'B-', value: 'B_NEGATIVO' },
    { label: 'AB+', value: 'AB_POSITIVO' },
    { label: 'AB-', value: 'AB_NEGATIVO' },
    { label: 'O+', value: 'O_POSITIVO' },
    { label: 'O-', value: 'O_NEGATIVO' },
  ];

  estadosDisponibles: FiltroEstado[] = [
    { label: 'Todas', value: null },
    { label: 'Programada', value: EstadoCita.PROGRAMADA },
    { label: 'Atendida', value: EstadoCita.ATENDIDA },
    { label: 'Cancelada', value: EstadoCita.CANCELADA },
    { label: 'No Asistió', value: EstadoCita.NO_ASISTIO },
  ];

  constructor(
    private citaService: CitaService,
    private horarioMedicoService: HorarioMedicoService,
    private toast: GlobalToast,
    private cdr: ChangeDetectorRef,
    private http: HttpClient,
  ) {}

  ngOnInit(): void {
    this.cargarCitas();
    this.cargarMedicos();
    this.cargarPacientes();
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
          : ((data as any).content ?? []);
        this.cdr.markForCheck();
      },
    });
  }

  cargarEspecialidades(): void {
    this.http.get<Especialidad[]>('http://localhost:8080/api/especialidad').subscribe({
      next: (data) => {
        this.especialidades = data;
        this.cdr.markForCheck();
      },
    });
  }

  aplicarFiltros(): void {
    let res = [...this.citas];

    if (this.terminoBusqueda.trim()) {
      const t = this.terminoBusqueda.toLowerCase();
      res = res.filter((c) => {
        const pac =
          `${c.pacienteNombre} ${c.pacienteApellidoPaterno} ${c.pacienteApellidoMaterno}`.toLowerCase();
        const med =
          `${c.medicoNombre} ${c.medicoApellidoPaterno} ${c.medicoApellidoMaterno}`.toLowerCase();
        return pac.includes(t) || med.includes(t) || c.pacienteDni.includes(t);
      });
    }

    if (this.filtroEstado) res = res.filter((c) => c.estado === this.filtroEstado);
    if (this.filtroMedico) res = res.filter((c) => c.medicoId === this.filtroMedico);

    if (this.filtroFechaInicio)
      res = res.filter((c) => new Date(c.fechaHora) >= this.filtroFechaInicio!);

    if (this.filtroFechaFin) {
      const fin = new Date(this.filtroFechaFin);
      fin.setHours(23, 59, 59, 999);
      res = res.filter((c) => new Date(c.fechaHora) <= fin);
    }

    this.citasFiltradas = res;
    this.initialValue = [...res];
    this.isSorted = null;
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
    this.nuevaCita = this.resetCitaForm();
    this.fechaHoraCita = null;
    this.especialidadSeleccionada = null;
    this.medicosFiltrados = [...this.medicos];
    this.resetDniSearch();
    this.mostrarModal = true;
  }

  private resetDniSearch(): void {
    this.dniPaciente = '';
    this.dniTouched = false;
    this.pacienteEncontrado = null;
    this.pacienteNoEncontrado = false;
    this.mostrarCamposPaciente = false;
    this.buscandoDni = false;
    this.reniecCargando = false;
    this.nuevoPaciente = {
      nombre: '',
      apellidoPaterno: '',
      apellidoMaterno: '',
      telefono: '',
      email: '',
      direccion: '',
      fechaNacimiento: '',
      genero: '',
      grupoSanguineo: '',
    };
  }

  get dniValido(): boolean {
    return /^\d{8}$/.test(this.dniPaciente?.trim());
  }

  get pacienteResuelto(): boolean {
    return !!this.pacienteEncontrado || this.nuevaCita.pacienteId > 0;
  }

  buscarPacientePorDni(): void {
    this.dniTouched = true;
    const dni = this.dniPaciente?.trim();
    if (!dni || !/^\d{8}$/.test(dni)) {
      this.toast.warn('Ingrese un DNI válido de 8 dígitos');
      return;
    }

    this.buscandoDni = true;
    this.pacienteEncontrado = null;
    this.pacienteNoEncontrado = false;
    this.mostrarCamposPaciente = false;
    this.nuevaCita.pacienteId = 0;

    this.citaService.buscarPacientePorDni(dni).subscribe({
      next: (paciente) => {
        if (paciente) {
          this.pacienteEncontrado = paciente;
          this.nuevaCita.pacienteId = paciente.id;
          this.buscandoDni = false;
          this.toast.success(`Paciente encontrado: ${paciente.nombre} ${paciente.apellidoPaterno}`);
        } else {
          this.consultarReniec(dni);
        }
        this.cdr.markForCheck();
      },
      error: () => {
        this.consultarReniec(dni);
      },
    });
  }

  private consultarReniec(dni: string): void {
    this.reniecCargando = true;

    this.citaService.consultarReniec(dni).subscribe({
      next: (data) => {
        this.pacienteNoEncontrado = true;
        this.mostrarCamposPaciente = true;
        if (data) {
          this.nuevoPaciente.nombre = data.nombres || data.first_name || '';
          this.nuevoPaciente.apellidoPaterno = data.apellidoPaterno || data.first_last_name || '';
          this.nuevoPaciente.apellidoMaterno = data.apellidoMaterno || data.second_last_name || '';
        }
        this.reniecCargando = false;
        this.buscandoDni = false;
        this.cdr.markForCheck();
      },
      error: () => {
        this.pacienteNoEncontrado = true;
        this.mostrarCamposPaciente = true;
        this.reniecCargando = false;
        this.buscandoDni = false;
        this.cdr.markForCheck();
      },
    });
  }

  get pacienteFormValido(): boolean {
    return !!(
      this.nuevoPaciente.nombre.trim() &&
      this.nuevoPaciente.apellidoPaterno.trim() &&
      this.nuevoPaciente.telefono &&
      /^\d{9}$/.test(this.nuevoPaciente.telefono) &&
      this.nuevoPaciente.fechaNacimiento &&
      this.nuevoPaciente.genero &&
      this.nuevoPaciente.grupoSanguineo
    );
  }

  registrarPacienteYContinuar(): void {
    if (!this.nuevoPaciente.nombre?.trim()) {
      this.toast.warn('Ingrese el nombre del paciente');
      return;
    }
    if (!this.nuevoPaciente.apellidoPaterno?.trim()) {
      this.toast.warn('Ingrese el apellido paterno');
      return;
    }
    if (!this.nuevoPaciente.apellidoMaterno?.trim()) {
      this.toast.warn('Ingrese el apellido materno');
      return;
    }
    if (!/^\d{9}$/.test(this.nuevoPaciente.telefono)) {
      this.toast.warn('El teléfono debe tener 9 dígitos');
      return;
    }
    if (!this.nuevoPaciente.fechaNacimiento) {
      this.toast.warn('Seleccione la fecha de nacimiento');
      return;
    }
    if (!this.nuevoPaciente.genero) {
      this.toast.warn('Seleccione el género');
      return;
    }
    if (!this.nuevoPaciente.grupoSanguineo) {
      this.toast.warn('Seleccione el grupo sanguíneo');
      return;
    }

    const payload = { dni: this.dniPaciente.trim(), ...this.nuevoPaciente };

    this.citaService.registrarPaciente(payload).subscribe({
      next: (res: any) => {
        const pacienteId = res?.id;
        if (pacienteId) this.nuevaCita.pacienteId = pacienteId;
        this.pacienteEncontrado = {
          id: pacienteId,
          dni: this.dniPaciente.trim(),
          ...this.nuevoPaciente,
        } as any;
        this.pacienteNoEncontrado = false;
        this.mostrarCamposPaciente = false;
        this.toast.success('Paciente registrado correctamente');
        this.cargarPacientes();
        this.cdr.markForCheck();
      },
      error: (err) => {
        this.toast.error(err?.error?.mensaje || 'Error al registrar paciente');
      },
    });
  }

  verDetalle(cita: CitaDetalle): void {
    this.citaSeleccionada = cita;
    this.mostrarDetalleModal = true;
  }

  abrirReprogramar(cita: CitaDetalle): void {
    this.citaSeleccionada = cita;
    this.nuevaFechaReprogramacion = new Date(cita.fechaHora);
    this.horarioMedicoService.getHorariosPorMedico(cita.medicoId).subscribe({
      next: (data) => {
        this.horariosMedico = data.filter((h) => h.activo);
        this.cdr.markForCheck();
      },
    });
    this.mostrarReprogramarModal = true;
  }

  onEspecialidadChange(): void {
    this.nuevaCita.medicoId = 0;
    this.horariosMedico = [];
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

  onMedicoChange(): void {
    this.horariosMedico = [];
    if (this.nuevaCita.medicoId && this.nuevaCita.medicoId > 0) {
      this.horarioMedicoService.getHorariosPorMedico(this.nuevaCita.medicoId).subscribe({
        next: (data) => {
          this.horariosMedico = data.filter((h) => h.activo);
          this.cdr.markForCheck();
        },
      });
    }
  }

  private dateToDiaSemana(date: Date): string {
    const dias = ['DOMINGO', 'LUNES', 'MARTES', 'MIERCOLES', 'JUEVES', 'VIERNES', 'SABADO'];
    return dias[date.getDay()];
  }

  get horariosInfo(): string {
    if (this.horariosMedico.length === 0) return '';
    const agrupado = new Map<string, string[]>();
    for (const h of this.horariosMedico) {
      if (!agrupado.has(h.dia)) agrupado.set(h.dia, []);
      agrupado.get(h.dia)!.push(`${h.horaInicio} - ${h.horaFin}`);
    }
    return Array.from(agrupado.entries())
      .map(([dia, horas]) => `${dia}: ${horas.join(', ')}`)
      .join(' | ');
  }

  private toLocalISOString(date: Date): string {
    const pad = (n: number) => String(n).padStart(2, '0');
    return `${date.getFullYear()}-${pad(date.getMonth() + 1)}-${pad(date.getDate())}T${pad(date.getHours())}:${pad(date.getMinutes())}:00`;
  }

  private horarioCitaValido(fecha: Date): boolean {
    if (this.horariosMedico.length === 0) return true;
    const diaSemana = this.dateToDiaSemana(fecha);
    const horaCita = `${String(fecha.getHours()).padStart(2, '0')}:${String(fecha.getMinutes()).padStart(2, '0')}`;
    return this.horariosMedico.some(
      (h) => h.dia === diaSemana && horaCita >= h.horaInicio && horaCita <= h.horaFin,
    );
  }

  get now(): Date {
    return new Date();
  }

  esFechaFutura(d: Date): boolean {
    return d > this.now;
  }

  get motivoInvalido(): boolean {
    const m = this.nuevaCita.motivoConsulta;
    return m.trim().length > 0 && m.trim().length < 5;
  }

  get formValido(): boolean {
    return !!(
      this.pacienteResuelto &&
      this.nuevaCita.medicoId &&
      this.nuevaCita.medicoId > 0 &&
      this.fechaHoraCita &&
      this.fechaHoraCita > this.now &&
      this.nuevaCita.motivoConsulta.trim().length >= 5
    );
  }

  guardarCita(): void {
    if (!this.nuevaCita.pacienteId || this.nuevaCita.pacienteId === 0) {
      this.toast.warn('Busque y seleccione un paciente por DNI');
      return;
    }
    if (!this.nuevaCita.medicoId) {
      this.toast.warn('Seleccione un médico');
      return;
    }
    if (!this.fechaHoraCita) {
      this.toast.warn('Seleccione fecha y hora');
      return;
    }
    if (this.fechaHoraCita <= new Date()) {
      this.toast.warn('La fecha debe ser posterior a la actual');
      return;
    }
    if (!this.nuevaCita.motivoConsulta?.trim()) {
      this.toast.warn('Ingrese el motivo de consulta');
      return;
    }
    if (this.nuevaCita.motivoConsulta.trim().length < 5) {
      this.toast.warn('El motivo debe tener al menos 5 caracteres');
      return;
    }

    if (this.horariosMedico.length > 0 && !this.horarioCitaValido(this.fechaHoraCita)) {
      this.toast.warn('El médico no atiende en la fecha y hora seleccionadas');
      return;
    }

    this.nuevaCita.fechaHora = this.toLocalISOString(this.fechaHoraCita);

    if (this.modoEdicion && this.citaEditandoId) {
      this.citaService.actualizar(this.citaEditandoId, this.nuevaCita).subscribe({
        next: (res) => {
          this.toast.success(res);
          this.cerrarModalYRecargar();
        },
        error: (err) => this.toast.error(err.error || 'Error al actualizar la cita'),
      });
    } else {
      this.citaService.registrar(this.nuevaCita).subscribe({
        next: (res) => {
          this.toast.success(res);
          this.cerrarModalYRecargar();
        },
        error: (err) => this.toast.error(err.error || 'Error al registrar la cita'),
      });
    }
  }

  reprogramarCita(): void {
    if (!this.citaSeleccionada || !this.nuevaFechaReprogramacion) {
      this.toast.warn('Seleccione una nueva fecha y hora');
      return;
    }
    if (this.nuevaFechaReprogramacion <= new Date()) {
      this.toast.warn('La nueva fecha debe ser posterior a la actual');
      return;
    }
    if (this.horariosMedico.length > 0 && !this.horarioCitaValido(this.nuevaFechaReprogramacion)) {
      this.toast.warn('El médico no atiende en la fecha y hora seleccionadas');
      return;
    }
    this.citaService
      .reprogramar(this.citaSeleccionada.id, this.toLocalISOString(this.nuevaFechaReprogramacion))
      .subscribe({
        next: (res) => {
          this.toast.success(res);
          this.mostrarReprogramarModal = false;
          this.cargarCitas();
        },
        error: (err) => this.toast.error(err.error || 'Error al reprogramar'),
      });
  }

  confirmarEliminar(cita: CitaDetalle): void {
    this.citaDeleteando = cita;
    this.mostrarConfirmarDelete = true;
  }

  eliminarCita(): void {
    if (!this.citaDeleteando) return;
    this.citaService.eliminar(this.citaDeleteando.id).subscribe({
      next: () => {
        this.toast.success('Cita eliminada correctamente');
        this.mostrarConfirmarDelete = false;
        this.citaDeleteando = null;
        this.cargarCitas();
      },
      error: (err) => this.toast.error(err?.error?.mensaje || 'Error al eliminar la cita'),
    });
  }

  confirmarCancelar(cita: CitaDetalle): void {
    this.citaCancelando = cita;
    this.mostrarConfirmarCancelar = true;
  }

  cancelarCita(): void {
    if (!this.citaCancelando) return;
    this.citaService.cancelar(this.citaCancelando.id).subscribe({
      next: (res) => {
        this.toast.success(res);
        this.mostrarConfirmarCancelar = false;
        this.citaCancelando = null;
        this.cargarCitas();
      },
      error: (err) => this.toast.error(err.error || 'Error al cancelar'),
    });
  }

  completarCita(cita: CitaDetalle): void {
    this.citaService.completar(cita.id).subscribe({
      next: (res) => {
        this.toast.success(res);
        this.cargarCitas();
      },
      error: (err) => this.toast.error(err.error || 'Error al completar'),
    });
  }

  marcarNoAsistio(cita: CitaDetalle): void {
    this.citaService.noAsistio(cita.id).subscribe({
      next: (res) => {
        this.toast.success(res);
        this.cargarCitas();
      },
      error: (err) => this.toast.error(err.error || 'Error al actualizar'),
    });
  }

  private cerrarModalYRecargar(): void {
    this.mostrarModal = false;
    this.cargarCitas();
  }

  private resetCitaForm(): CitaDTO {
    return { pacienteId: 0, medicoId: 0, fechaHora: '', motivoConsulta: '' };
  }

  nombrePaciente(cita: CitaDetalle): string {
    return `${cita.pacienteNombre} ${cita.pacienteApellidoPaterno} ${cita.pacienteApellidoMaterno}`;
  }

  nombreMedico(cita: CitaDetalle): string {
    return `${cita.medicoNombre} ${cita.medicoApellidoPaterno} ${cita.medicoApellidoMaterno}`;
  }

  obtenerNombreCompleto(entidad: any): string {
    if (!entidad) return '';
    return `${entidad.nombre ?? ''} ${entidad.apellidoPaterno ?? ''} ${entidad.apellidoMaterno ?? ''}`.trim();
  }

  obtenerClaseEstado(estado: EstadoCita): string {
    const map: Record<EstadoCita, string> = {
      [EstadoCita.PROGRAMADA]: 'bg-teal-100 text-teal-800',
      [EstadoCita.ATENDIDA]: 'bg-green-100 text-green-800',
      [EstadoCita.CANCELADA]: 'bg-red-100 text-red-800',
      [EstadoCita.NO_ASISTIO]: 'bg-amber-100 text-amber-800',
    };
    return map[estado] ?? '';
  }

  puedeReprogramar = (c: CitaDetalle) => c.estado === EstadoCita.PROGRAMADA;
  puedeCancelar = (c: CitaDetalle) => c.estado === EstadoCita.PROGRAMADA;
  puedeCompletar = (c: CitaDetalle) => c.estado === EstadoCita.PROGRAMADA;
  puedeMarcarNoAsistio = (c: CitaDetalle) => c.estado === EstadoCita.PROGRAMADA;

  // Validation helpers for HTML
  isInvalid(value: string | null | undefined): boolean {
    return !value?.trim();
  }

  emailValido(email: string): boolean {
    return !email || /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email);
  }

  telefonoValido(tel: string): boolean {
    return /^\d{9}$/.test(tel);
  }

  dni8Valido(dni: string): boolean {
    return /^\d{8}$/.test(dni);
  }

  /*
   * =====================================
   *      LOGICA PARA HACER EL SORT
   * =====================================
   * */

  private resetting = false;

  customSort(event: SortEvent): void {
    if (this.resetting) return;

    if (this.isSorted == null || this.isSorted === undefined) {
      this.isSorted = true;
      this.sortTableData(event);
    } else if (this.isSorted === true) {
      this.isSorted = false;
      this.sortTableData(event);
    } else {
      this.isSorted = null;
      this.resetting = true;
      this.citasFiltradas = [...this.initialValue];
      this.dt.reset();
      setTimeout(() => {
        this.resetting = false;
      }, 0);
    }
  }

  private sortTableData(event: SortEvent): void {
    this.citasFiltradas.sort((data1, data2) => {
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
}
