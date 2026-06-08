export interface Usuario {
  id?: number;
  userName: string;
  password?: string;
  rol: Rol;
  activo: boolean;
}

export interface UsuarioDTO {
  userName: string;
  password: string;
  rol: Rol;
}

export enum Rol {
  ADMIN = 'ADMIN',
  MEDICO = 'MEDICO',
  RECEPCIONISTA = 'RECEPCIONISTA'
}
