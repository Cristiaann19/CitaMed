import { ChangeDetectionStrategy, ChangeDetectorRef, Component, OnInit, ViewChild } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { HttpClient } from '@angular/common/http';
import { DialogModule } from 'primeng/dialog';
import { ButtonModule } from 'primeng/button';
import { MedicoService } from '../../../core/services/medico-service';
import { Medico, MedicoDTO } from '../../../model/Medico';
import { Especialidad } from '../../../model/Especialidad';
import { Consultorio } from '../../../model/Consultorio';
import { Usuario, Rol } from '../../../model/Usuario';
import { GlobalToast } from '../../../core/services/global-toast';
import {
  LucideAngularModule,
  Stethoscope,
  Heart,
  Baby,
  Venus,
  Bone,
  Brain,
  Scan,
  Activity,
  Pill,
  Eye,
  BrainCircuit,
  Droplets,
  Shield,
  Ear,
  Accessibility,
  User,
} from 'lucide-angular';
import { PaginatorModule } from 'primeng/paginator';
import { Table, TableModule } from 'primeng/table';
import { SortEvent } from 'primeng/api';

@Component({
  selector: 'app-medicos',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    DialogModule,
    ButtonModule,
    TableModule,
    PaginatorModule,
    LucideAngularModule,
  ],
  templateUrl: './medicos-component.html',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class MedicosComponent implements OnInit {
  constructor(
    private medicoService: MedicoService,
    private cdr: ChangeDetectorRef,
    private toast: GlobalToast,
    private http: HttpClient,
  ) {}

  Stethoscope = Stethoscope;
  Heart = Heart;
  Baby = Baby;
  Venus = Venus;
  Bone = Bone;
  Brain = Brain;
  Scan = Scan;
  Activity = Activity;
  Pill = Pill;
  Eye = Eye;
  BrainCircuit = BrainCircuit;
  Droplets = Droplets;
  Shield = Shield;
  Ear = Ear;
  Accessibility = Accessibility;
  User = User;

  @ViewChild('dt') dt!: Table;
  initialValue: Medico[] = [];
  isSorted: boolean | null = null;
  private resetting = false;

  medicos: Medico[] = [];
  medicosFiltrados: Medico[] = [];
  especialidades: Especialidad[] = [];
  consultorios: Consultorio[] = [];
  mensaje = '';
  terminoBusqueda = '';
  filtroEspecialidad = '';
  mostrarModal = false;
  modoEdicion = false;
  medicoEditandoId: number | null = null;
  roles = Object.values(Rol);

  nuevoUsuario: Usuario = {
    userName: '',
    password: '',
    rol: Rol.MEDICO,
    activo: true,
  };

  nuevoMedico: MedicoDTO = {
    nombre: '',
    apellidoPaterno: '',
    apellidoMaterno: '',
    dni: '',
    telefono: '',
    direccion: '',
    email: '',
    fechaNacimiento: '',
    genero: '',
    numeroColegiatura: '',
    especialidadId: 0,
    userName: '',
    password: '',
  };

  ngOnInit(): void {
    this.obtenerMedicos();
    this.obtenerEspecialidades();
    this.obtenerConsultorios();
  }

  obtenerMedicos(): void {
    this.medicoService.listar().subscribe({
      next: (data: Medico[]) => {
        this.medicos = data;
        this.medicosFiltrados = [...data];
        this.initialValue = [...data];
        this.isSorted = null;
        this.cdr.markForCheck();
      },
      error: (err: any) => {
        console.error(err);
        this.mensaje = 'No se pudieron cargar los médicos';
        this.cdr.markForCheck();
      },
    });
  }

  obtenerEspecialidades(): void {
    this.medicoService.listarEspecialidades().subscribe({
      next: (data: Especialidad[]) => {
        this.especialidades = data;
        this.cdr.markForCheck();
      },
      error: (err: any) => {
        console.error(err);
        this.mensaje = 'No se pudieron cargar las especialidades';
        this.cdr.markForCheck();
      },
    });
  }

  obtenerConsultorios(): void {
    this.http.get<Consultorio[]>('http://localhost:8080/api/consultorio').subscribe({
      next: (data) => { this.consultorios = data; this.cdr.markForCheck(); },
      error: () => {},
    });
  }

  abrirNuevoMedico(): void {
    this.modoEdicion = false;
    this.medicoEditandoId = null;
    this.resetFormulario();
    this.mostrarModal = true;
    this.cdr.markForCheck();
  }

  editarMedico(medico: Medico): void {
    this.modoEdicion = true;
    this.medicoEditandoId = medico.id ?? null;
    this.nuevoMedico = {
      nombre: medico.nombre,
      apellidoPaterno: medico.apellidoPaterno,
      apellidoMaterno: medico.apellidoMaterno,
      dni: medico.dni,
      telefono: medico.telefono,
      direccion: medico.direccion,
      email: medico.email,
      fechaNacimiento: medico.fechaNacimiento,
      genero: medico.genero,
      numeroColegiatura: medico.numeroColegiatura,
      especialidadId: medico.especialidad.id,
      consultorioId: medico.consultorio?.id,
      userName: medico.userName,
      password: '',
    };

    this.nuevoUsuario = {
      userName: medico.userName,
      password: '',
      rol: Rol.MEDICO,
      activo: medico.activo ?? true,
    };

    this.mostrarModal = true;
    this.cdr.markForCheck();
  }

  registrarMedico(): void {
    //VALIDACIONES
    if (!this.nuevoMedico.nombre?.trim()) {
      this.toast.warn('Ingrese el nombre');
      this.cdr.markForCheck();
      return;
    }

    if (!this.nuevoMedico.apellidoPaterno?.trim()) {
      this.toast.warn('Ingrese el apellido paterno');
      this.cdr.markForCheck();
      return;
    }

    if (!this.nuevoMedico.apellidoMaterno?.trim()) {
      this.toast.warn('Ingrese el apellido materno');
      this.cdr.markForCheck();
      return;
    }

    if (!/^\d{8}$/.test(this.nuevoMedico.dni)) {
      this.toast.warn('El DNI debe tener 8 dígitos');
      this.cdr.markForCheck();
      return;
    }

    if (!/^\d{9}$/.test(this.nuevoMedico.telefono)) {
      this.toast.warn('El teléfono debe tener 9 dígitos');
      this.cdr.markForCheck();
      return;
    }

    if (!this.nuevoMedico.fechaNacimiento) {
      this.toast.warn('Seleccione la fecha de nacimiento');
      this.cdr.markForCheck();
      return;
    }

    if (!this.nuevoMedico.genero) {
      this.toast.warn('Seleccione el género');
      this.cdr.markForCheck();
      return;
    }

    if (!this.nuevoMedico.especialidadId) {
      this.toast.warn('Seleccione una especialidad');
      this.cdr.markForCheck();
      return;
    }

    const cmpLimpio = this.nuevoMedico.numeroColegiatura?.replace(/\D/g, '');
    if (!cmpLimpio || cmpLimpio.length < 5) {
      this.toast.warn('Ingrese un CMP válido');
      this.cdr.markForCheck();
      return;
    }

    this.nuevoMedico.numeroColegiatura = `CMP-${cmpLimpio.padStart(6, '0')}`;

    this.nuevoMedico.userName = (this.nuevoMedico.nombre + '_' + this.nuevoMedico.apellidoPaterno)
      .toLowerCase()
      .normalize('NFD')
      .replace(/[\u0300-\u036f]/g, '')
      .replace(/\s+/g, '')
      .replace(/[^a-z0-9._]/g, '');

    this.nuevoMedico.email =
      (this.nuevoMedico.nombre + '.' + this.nuevoMedico.apellidoPaterno)
        .toLowerCase()
        .normalize('NFD')
        .replace(/[\u0300-\u036f]/g, '')
        .replace(/\s+/g, '')
        .replace(/[^a-z0-9.]/g, '') + '@citamed.com';

    this.nuevoMedico.password = this.nuevoMedico.dni;

    if (!this.nuevoMedico.direccion) {
      this.nuevoMedico.direccion = '';
    }

    if (this.modoEdicion && this.medicoEditandoId !== null) {
      this.medicoService.modificar(this.medicoEditandoId, this.nuevoMedico).subscribe({
        next: (res: any) => {
          this.toast.success(res.mensaje || res);
          this.finalizarGuardado();
        },
        error: (err: any) => {
          console.error(err);
          this.toast.error('Error al actualizar el médico');
          this.cdr.markForCheck();
        },
      });
    } else {
      this.medicoService.registrar(this.nuevoMedico).subscribe({
        next: (res: any) => {
          this.toast.success(res);
          this.finalizarGuardado();
        },
        error: (err: any) => {
          console.error(err);
          this.toast.error('Error al registrar el médico');
          this.cdr.markForCheck();
        },
      });
    }
  }

  private finalizarGuardado(): void {
    this.obtenerMedicos();
    this.resetFormulario();
    this.mostrarModal = false;
    this.cdr.markForCheck();
  }

  cambiarEstado(medico: any): void {
    this.medicoService.cambiarEstado(medico.id).subscribe({
      next: (res: any) => {
        medico.activo = !medico.activo;
        this.toast.success(res.mensaje);
        this.cdr.markForCheck();
      },

      error: (err: any) => {
        console.error(err);
        this.toast.error('No se pudo cambiar el estado');
        this.cdr.markForCheck();
      },
    });
  }

  filtrar(): void {
    const texto = this.terminoBusqueda.toLowerCase();
    const especialidad = this.filtroEspecialidad.toLowerCase();
    this.medicosFiltrados = this.medicos.filter((m) => {
      const nombreCompleto = `${m.nombre} ${m.apellidoPaterno} ${m.apellidoMaterno}`.toLowerCase();
      const coincideTexto =
        nombreCompleto.includes(texto) ||
        m.numeroColegiatura.toLowerCase().includes(texto) ||
        m.email.toLowerCase().includes(texto) ||
        m.telefono.toLowerCase().includes(texto);
      const coincideEspecialidad =
        especialidad === '' || m.especialidad.nombre.toLowerCase().includes(especialidad);
      return coincideTexto && coincideEspecialidad;
    });

    this.initialValue = [...this.medicosFiltrados];
    this.isSorted = null;
    this.cdr.markForCheck();
  }

  obtenerIconoEspecialidad(nombre: string = ''): any {
    if (!nombre) return this.Stethoscope;

    const key = nombre
      .toLowerCase()
      .normalize('NFD')
      .replace(/[\u0300-\u036f]/g, '')
      .trim();

    const mapaIconos: Record<string, any> = {
      'medicina general': this.Stethoscope,
      cardiologia: this.Heart,
      pediatria: this.Baby,
      ginecologia: this.Venus,
      'ginecologia y obstetricia': this.Venus,
      traumatologia: this.Bone,
      'traumatologia y ortopedia': this.Bone,
      neurologia: this.Brain,
      dermatologia: this.Scan,
      endocrinologia: this.Activity,
      gastroenterologia: this.Pill,
      oftalmologia: this.Eye,
      psiquiatria: this.BrainCircuit,
      nefrologia: this.Droplets,
      urologia: this.Shield,
      otorrinolaringologia: this.Ear,
      reumatologia: this.Accessibility,
    };

    return mapaIconos[key] ?? this.Stethoscope;
  }

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
      this.medicosFiltrados = [...this.initialValue];
      this.dt.reset();
      setTimeout(() => {
        this.resetting = false;
      }, 0);
    }
  }

  private sortTableData(event: SortEvent): void {
    this.medicosFiltrados.sort((data1, data2) => {
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

  resetFormulario(): void {
    this.nuevoMedico = {
      nombre: '',
      apellidoPaterno: '',
      apellidoMaterno: '',
      dni: '',
      telefono: '',
      direccion: '',
      email: '',
      fechaNacimiento: '',
      genero: '',
      numeroColegiatura: '',
      especialidadId: 0,
      consultorioId: undefined,
      userName: '',
      password: '',
    };

    this.nuevoUsuario = {
      userName: '',
      password: '',
      rol: Rol.MEDICO,
      activo: true,
    };

    this.cdr.markForCheck();
  }
}
