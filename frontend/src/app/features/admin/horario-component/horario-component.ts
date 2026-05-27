import {
  ChangeDetectionStrategy,
  ChangeDetectorRef,
  Component,
  OnInit,
  inject,
} from '@angular/core';
import { CommonModule, DecimalPipe, SlicePipe } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { DialogModule } from 'primeng/dialog';
import { ButtonModule } from 'primeng/button';
import { PaginatorModule } from 'primeng/paginator';
import { GlobalToast } from '../../../core/services/global-toast';
import { AuthService } from '../../../core/services/auth-service';
import {
  HorarioMedicoService,
  Medico,
  HorarioMedico,
  HorarioMedicoDTO,
  DiaSemana,
  Consultorio,
} from '../../../core/services/horario-medico-service';

@Component({
  selector: 'app-horario-component',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    DialogModule,
    ButtonModule,
    DecimalPipe,
    SlicePipe,
    PaginatorModule,
  ],
  templateUrl: './horario-component.html',
  styleUrl: './horario-component.css',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class HorarioComponent implements OnInit {
  public authService = inject(AuthService);

  constructor(
    private svc: HorarioMedicoService,
    private cdr: ChangeDetectorRef,
    private toast: GlobalToast,
  ) { }

  medicos: Medico[] = [];
  medicosFiltrados: Medico[] = [];
  medicosMostrados: Medico[] = [];
  consultorios: Consultorio[] = [];
  horariosPorMedico: Record<number, HorarioMedico[]> = {};
  medicoIdParaBorrar: number | null = null;

  terminoBusqueda = '';
  filtroEspecialidad = '';

  displayModal = false;
  displayDelete = false;
  modoEdicion = false;
  horarioEditandoId: number | null = null;

  first: number = 0;
  rows: number = 6;

  horarioForm: HorarioMedicoDTO = {
    medicoId: 0,
    consultorioId: 0,
    dia: 'LUNES',
    horaInicio: '',
    horaFin: '',
  };

  diasSemana: { valor: DiaSemana; label: string; corto: string }[] = [
    { valor: 'LUNES', label: 'Lunes', corto: 'Lun' },
    { valor: 'MARTES', label: 'Martes', corto: 'Mar' },
    { valor: 'MIERCOLES', label: 'Miércoles', corto: 'Mié' },
    { valor: 'JUEVES', label: 'Jueves', corto: 'Jue' },
    { valor: 'VIERNES', label: 'Viernes', corto: 'Vie' },
    { valor: 'SABADO', label: 'Sábado', corto: 'Sáb' },
    { valor: 'DOMINGO', label: 'Domingo', corto: 'Dom' },
  ];

  ngOnInit(): void {
    this.cargarMedicos();
    this.cargarConsultorios();
  }

  cargarMedicos(): void {
    this.svc.getMedicos().subscribe({
      next: (data) => {
        Promise.resolve().then(() => {
          this.medicos = data.filter((m) => m.activo);
          this.medicosFiltrados = [...this.medicos];
          this.actualizarVista();
          this.medicos.forEach((m) => this.cargarHorarios(m.id));
          this.cdr.markForCheck();
        });
      },
      error: (err) => {
        console.error(err);
        this.toast.error('No se pudieron cargar los médicos. Intenta de nuevo.', {
          summary: 'Error al cargar',
        });
      },
    });
  }

  cargarConsultorios(): void {
    this.svc.getConsultorios().subscribe({
      next: (data) => {
        this.consultorios = data;
        this.cdr.markForCheck();
      },
      error: (err) => {
        console.error(err);
        this.toast.error('No se pudieron cargar los consultorios.', {
          summary: 'Error al cargar',
        });
      },
    });
  }

  cargarHorarios(medicoId: number): void {
    this.svc.getHorariosPorMedico(medicoId).subscribe({
      next: (data) => {
        this.horariosPorMedico[medicoId] = data;
        this.cdr.markForCheck();
      },
      error: (err) => {
        console.error(err);
      },
    });
  }

  getHorariosDia(medicoId: number, dia: DiaSemana): HorarioMedico[] {
    return (this.horariosPorMedico[medicoId] ?? []).filter((h) => h.dia === dia && h.activo);
  }

  calcularHorasSemana(medicoId: number): number {
    const horarios = this.horariosPorMedico[medicoId] ?? [];
    return horarios
      .filter((h) => h.activo)
      .reduce((acc, h) => {
        const [hi, mi] = h.horaInicio.split(':').map(Number);
        const [hf, mf] = h.horaFin.split(':').map(Number);
        return acc + (hf * 60 + mf - (hi * 60 + mi)) / 60;
      }, 0);
  }

  iniciales(medico: Medico): string {
    return (medico.nombre.charAt(0) + medico.apellidoPaterno.charAt(0)).toUpperCase();
  }

  filtrar(): void {
    const term = this.terminoBusqueda.toLowerCase();
    const esp = this.filtroEspecialidad.toLowerCase();
    this.medicosFiltrados = this.medicos.filter((m) => {
      const nombre = `${m.nombre} ${m.apellidoPaterno}`.toLowerCase();
      const matchNombre = nombre.includes(term);
      const matchEsp = esp ? m.especialidad?.nombre?.toLowerCase().includes(esp) : true;
      return matchNombre && matchEsp;
    });
    this.first = 0;
    this.cdr.markForCheck();
    this.actualizarVista();
  }

  onPageChange(event: any): void {
    this.first = event.first;
    this.actualizarVista();
  }

  actualizarVista(): void {
    this.medicosMostrados = this.medicosFiltrados.slice(this.first, this.first + this.rows);
  }

  abrirNuevoTurno(medicoId: number): void {
    this.modoEdicion = false;
    this.horarioEditandoId = null;
    this.horarioForm = {
      medicoId,
      consultorioId: this.consultorios[0]?.id ?? 0,
      dia: 'LUNES',
      horaInicio: '',
      horaFin: '',
    };
    this.displayModal = true;
  }

  abrirConfirmacion(horario: HorarioMedico, medicoId: number): void {
    this.horarioEditandoId = horario.id;
    this.medicoIdParaBorrar = medicoId;
    this.displayDelete = true;
  }

  abrirEditarTurno(horario: HorarioMedico, medicoId: number): void {
    this.modoEdicion = true;
    this.horarioEditandoId = horario.id;
    this.displayModal = false;

    this.horarioForm = {
      medicoId: medicoId,
      consultorioId: horario.consultorio?.id ?? 0,
      dia: horario.dia,
      horaInicio: horario.horaInicio.slice(0, 5),
      horaFin: horario.horaFin.slice(0, 5),
    };

    Promise.resolve().then(() => {
      this.displayModal = true;
      this.cdr.markForCheck();
    });
  }

  guardar(): void {
    if (this.modoEdicion && this.horarioEditandoId !== null) {
      this.svc.actualizarHorario(this.horarioEditandoId, this.horarioForm).subscribe({
        next: (res) => {
          this.cargarHorarios(this.horarioForm.medicoId);
          this.displayModal = false;
          this.toast.success('Horario actualizado correctamente');
          this.cdr.markForCheck();
        },
        error: (err) => this.toast.error('Error al actualizar')
      });
    } else {
      this.crearNuevo();
    }
  }

  private crearNuevo(): void {
    this.svc.crearHorario(this.horarioForm).subscribe({
      next: () => {
        this.cargarHorarios(this.horarioForm.medicoId);
        this.displayModal = false;
        this.cdr.markForCheck();

        if (this.modoEdicion) {
          this.toast.success('El turno fue modificado correctamente.', {
            summary: 'Turno actualizado',
          });
        } else {
          this.toast.success('El nuevo turno fue registrado correctamente.', {
            summary: 'Turno agregado',
          });
        }
      },
      error: (err) => {
        console.error(err);
        this.toast.error(
          this.modoEdicion
            ? 'No se pudo guardar el turno modificado.'
            : 'No se pudo registrar el nuevo turno.',
          { summary: 'Error al guardar' },
        );
      },
    });
  }

  confirmarEliminado(): void {
    if (this.horarioEditandoId && this.medicoIdParaBorrar) {
      this.svc.toggleActivo(this.horarioEditandoId).subscribe({
        next: () => {
          this.cargarHorarios(this.medicoIdParaBorrar!);
          this.displayDelete = false;
          this.toast.success('Eliminado correctamente');
          this.cdr.markForCheck();
        },
        error: (err) => console.error(err)
      });
    }
  }
}
