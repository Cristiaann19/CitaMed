import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HttpClient } from '@angular/common/http';
import { Especialidad } from '../../../model/Especialidad';

@Component({
  selector: 'app-especialidades-component',
  standalone: true,
  imports: [CommonModule],
  template: `
<div class="p-1">
  <div class="bg-white rounded-3xl shadow-sm border border-blue-100/50 p-8 mb-6">
    <div class="flex flex-col lg:flex-row justify-between items-start lg:items-center gap-5">
      <div>
        <h3 class="text-4xl font-bold text-[#329791] mb-2">Especialidades M&eacute;dicas</h3>
        <p class="text-gray-600 text-sm">Listado de especialidades disponibles en el sistema</p>
      </div>
    </div>
  </div>

  <div *ngIf="error" class="bg-red-50 border-l-4 border-red-500 p-4 rounded-lg mb-6">
    <p class="text-red-700 text-sm">{{ error }}</p>
  </div>

  <div *ngIf="cargando" class="bg-white rounded-3xl shadow-sm border border-gray-100 p-20 text-center">
    <p class="font-semibold text-lg text-gray-400">Cargando especialidades...</p>
  </div>

  <div *ngIf="!cargando" class="bg-white rounded-3xl shadow-sm border border-gray-100 overflow-hidden">
    <table class="w-full">
      <thead>
        <tr class="bg-[#e4f1f1]/20 border-b border-[#e4f1f1]/10">
          <th class="text-left px-6 py-4 text-xs font-black uppercase tracking-wider text-[#329791]">#</th>
          <th class="text-left px-6 py-4 text-xs font-black uppercase tracking-wider text-[#329791]">Nombre</th>
          <th class="text-left px-6 py-4 text-xs font-black uppercase tracking-wider text-[#329791]">Descripci&oacute;n</th>
        </tr>
      </thead>
      <tbody>
        <tr *ngFor="let esp of especialidades; let i = index" class="border-b border-gray-100 hover:bg-[#329791]/[0.03] transition-all">
          <td class="px-6 py-5"><span class="font-bold text-[#329791]">{{ i + 1 }}</span></td>
          <td class="px-6 py-5">
            <span class="inline-block px-3 py-1.5 rounded-xl bg-[#329791]/10 text-[#329791] text-xs font-bold">{{ esp.nombre }}</span>
          </td>
          <td class="px-6 py-5"><span class="text-sm text-gray-700">{{ esp.descripcion }}</span></td>
        </tr>
        <tr *ngIf="especialidades.length === 0">
          <td colspan="3" class="text-center py-20 text-gray-400">
            <p class="font-semibold text-lg">No hay especialidades registradas</p>
          </td>
        </tr>
      </tbody>
    </table>
  </div>
</div>
  `,
})
export class EspecialidadesComponent implements OnInit {
  especialidades: Especialidad[] = [];
  cargando = true;
  error = '';

  constructor(
    private http: HttpClient,
    private cdr: ChangeDetectorRef,
  ) {}

  ngOnInit(): void {
    setTimeout(() => {
      if (this.cargando) {
        this.cargando = false;
        this.error = 'El servidor no respondi&oacute; a tiempo';
        this.cdr.detectChanges();
      }
    }, 5000);

    this.http.get<Especialidad[]>('http://localhost:8080/api/especialidad').subscribe({
      next: (data) => {
        this.especialidades = data;
        this.cargando = false;
        this.cdr.detectChanges();
      },
      error: (err) => {
        this.cargando = false;
        this.error = 'Error al cargar: ' + (err.status ? 'HTTP ' + err.status : err.message);
        this.cdr.detectChanges();
      },
    });
  }
}
