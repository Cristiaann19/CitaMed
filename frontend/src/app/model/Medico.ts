import { Especialidad } from './Especialidad';
import { Consultorio } from './Consultorio';

export interface Medico {
  id?: number;
  nombre: string;
  apellidoPaterno: string;
  apellidoMaterno: string;
  dni: string;
  telefono: string;
  direccion: string;
  email: string;
  fechaNacimiento: string;
  genero: string;
  numeroColegiatura: string;
  userName: string;
  activo: boolean;
  especialidad: Especialidad;
  consultorio?: Consultorio;
}

export interface MedicoDTO {
  nombre: string;
  apellidoPaterno: string;
  apellidoMaterno: string;
  dni: string;
  telefono: string;
  direccion: string;
  email: string;
  fechaNacimiento: string;
  genero: string;
  numeroColegiatura: string;
  especialidadId: number;
  consultorioId?: number;
  userName: string;
  password: string;
}