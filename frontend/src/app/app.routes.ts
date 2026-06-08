import { Routes } from '@angular/router';
import { LoginComponent } from './features/auth/login-component/login-component';
import { HomeComponent } from './features/home/home-component';
import { DashboardLayout } from './layout/dashboard-layout/dashboard-layout';
import { UsuarioComponent } from './features/admin/usuario-component/usuario-component';
import { DashboardComponent } from './features/admin/dashboard-component/dashboard-component';
import { CitasComponent } from './features/admin/citas-component/citas-component';
import { MedicosComponent } from './features/admin/medicos-component/medicos-component';
import { PacientesComponent } from './features/admin/pacientes-component/pacientes-component';
import { EspecialidadesComponent } from './features/admin/especialidades-component/especialidades-component';
import { HorarioComponent } from './features/admin/horario-component/horario-component';
import { EmpleadoComponent } from './features/admin/empleado-component/empleado-component';
import { PagosComponent } from './features/admin/pagos-component/pagos-component';
import { Reportes } from './features/admin/reportes/reportes';
import { HistorialMedicoC } from './features/admin/historial-medico-c/historial-medico-c';
import { authGuard } from './core/guards/auth.guard';
import { Page404Component } from './shared/components/page404-component/page404-component';

export const routes: Routes = [
  {
    path: 'inicio',
    component: HomeComponent,
    title: 'Citamed - Bienvenidos',
  },
  {
    path: 'login',
    component: LoginComponent,
    title: 'Iniciar Sesión',
  },
  {
    path: 'admin',
    component: DashboardLayout,
    title: 'Dashboard',
    canActivate: [authGuard],
    children: [
      { path: '', redirectTo: 'dashboard', pathMatch: 'full' },
      {
        path: 'usuarios',
        component: UsuarioComponent,
        data: { title: 'Usuarios', roles: ['ADMIN'] },
        canActivate: [() => import('./core/guards/role.guard').then((m) => m.roleGuard as any)],
      },
      { path: 'dashboard', component: DashboardComponent, data: { title: 'Dashboard' } },
      {
        path: 'citas',
        component: CitasComponent,
        data: { title: 'Citas', roles: ['ADMIN', 'MEDICO', 'RECEPCIONISTA'] },
        canActivate: [() => import('./core/guards/role.guard').then((m) => m.roleGuard as any)],
      },
      {
        path: 'medicos',
        component: MedicosComponent,
        data: { title: 'Médicos', roles: ['ADMIN'] },
        canActivate: [() => import('./core/guards/role.guard').then((m) => m.roleGuard as any)],
      },
      {
        path: 'pacientes',
        component: PacientesComponent,
        data: { title: 'Pacientes', roles: ['ADMIN', 'MEDICO', 'RECEPCIONISTA'] },
        canActivate: [() => import('./core/guards/role.guard').then((m) => m.roleGuard as any)],
      },
      {
        path: 'especialidades',
        component: EspecialidadesComponent,
        data: { title: 'Especialidades', roles: ['ADMIN', 'MEDICO', 'RECEPCIONISTA'] },
        canActivate: [() => import('./core/guards/role.guard').then((m) => m.roleGuard as any)],
      },
      {
        path: 'horarios',
        component: HorarioComponent,
        data: { title: 'Horarios', roles: ['ADMIN', 'MEDICO'] },
        canActivate: [() => import('./core/guards/role.guard').then((m) => m.roleGuard as any)],
      },
      {
        path: 'empleados',
        component: EmpleadoComponent,
        data: { title: 'Empleados', roles: ['ADMIN'] },
        canActivate: [() => import('./core/guards/role.guard').then((m) => m.roleGuard as any)],
      },
      {
        path: 'pagos',
        component: PagosComponent,
        data: { title: 'Pagos', roles: ['ADMIN'] },
        canActivate: [() => import('./core/guards/role.guard').then((m) => m.roleGuard as any)],
      },
      {
        path: 'reportes',
        component: Reportes,
        data: { title: 'Reportes', roles: ['ADMIN'] },
        canActivate: [() => import('./core/guards/role.guard').then((m) => m.roleGuard as any)],
      },
      {
        path: 'historial-medico',
        component: HistorialMedicoC,
        data: { title: 'Historial Médico', roles: ['ADMIN'] },
        canActivate: [() => import('./core/guards/role.guard').then((m) => m.roleGuard as any)],
      },
    ],
  },
  { path: '', redirectTo: 'inicio', pathMatch: 'full' },
  { path: '**', component: Page404Component },
];
