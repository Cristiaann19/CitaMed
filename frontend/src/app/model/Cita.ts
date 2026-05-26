export interface CitaDetalle {
  id: number;

  pacienteId: number;
  pacienteNombre: string;
  pacienteApellidoPaterno: string;
  pacienteApellidoMaterno: string;
  pacienteDni: string;

  // Médico
  medicoId: number;
  medicoNombre: string;
  medicoApellidoPaterno: string;
  medicoApellidoMaterno: string;
  medicoEspecialidad: string;

  // Consultorio
  consultorioId: number;
  consultorioNumero: string;
  consultorioDescripcion: string;

  fechaHora: string;
  motivoConsulta: string;
  estado: EstadoCita;
}

export interface CitaDTO {
  pacienteId: number;
  medicoId: number;
  fechaHora: string;
  motivoConsulta: string;
}

export enum EstadoCita {
  PROGRAMADA  = 'PROGRAMADA',
  ATENDIDA    = 'ATENDIDA',
  CANCELADA   = 'CANCELADA',
  NO_ASISTIO  = 'NO_ASISTIO',
}

export interface Cita {
  id: number;
  paciente: {
    id: number;
    nombre: string;
    apellidoPaterno: string;
    apellidoMaterno: string;
    dni: string;
  };
  medico: {
    id: number;
    nombre: string;
    apellidoPaterno: string;
    apellidoMaterno: string;
    especialidad: { id: number; nombre: string };
  };
  consultorio: {
    id: number;
    numero: string;
    descripcion: string;
  };
  fechaHora: string;
  motivoConsulta: string;
  estado: EstadoCita;
}
