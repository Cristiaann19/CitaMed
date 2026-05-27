# Documentación CitaMed

Sistema de Gestion de Consultas Medicas - CitaMed

---

## 1. Landing Page

Pagina principal publica del sistema.

Hero:

![hero](capturas/lading1.png)

Especialidades:

![especialidades](capturas/lading2.png)

Doctor destacado:

![doctor](capturas/lading3.png)

Testimonios:

![testimonios](capturas/lading4.png)

Contacto / Footer:

![contacto](capturas/lading5.png)

---

## 2. Login

Autenticacion de usuarios con JWT.

![login](capturas/login.png)

---

## 3. Dashboard

Vista principal con metricas del sistema.

Dashboard parte 1 - cards de estadisticas:

![dashboard1](capturas/dashboard1.png)

Dashboard parte 2 - ultimas citas, especialidades, agenda, medicos, pagos:

![dashboard2](capturas/dashboard2.png)

El dashboard muestra:
- Citas de hoy
- Pacientes activos del mes
- Ingresos del mes
- Citas canceladas de la semana
- Total de pacientes registrados
- Total de medicos registrados
- Total de citas programadas
- Ultimas citas registradas
- Especialidades mas solicitadas
- Agenda del dia
- Medicos activos (top por citas)
- Ultimos pagos

---

## 4. Gestion de Pacientes

CRUD completo de pacientes.

![pacientes](capturas/pacientes.png)

Funcionalidades:
- Listar pacientes con paginacion
- Buscar por nombre, apellido o DNI
- Incluir pacientes inactivos
- Registrar nuevo paciente
- Editar datos del paciente
- Eliminar (soft-delete) / Activar / Desactivar

---

## 5. Gestion de Medicos

CRUD completo de medicos.

![medicos](capturas/medicos.png)

Funcionalidades:
- Listar medicos con paginacion
- Buscar por nombre o DNI
- Filtrar por especialidad
- Registrar nuevo medico
- Editar datos del medico
- Cambiar especialidad
- Activar / Desactivar medico

---

## 6. Gestion de Citas

CRUD completo y operaciones de estado.

Listado de citas:

![citas](capturas/citas.png)

Detalle de cita:

![detalle](capturas/citaInfo.png)

Informacion adicional:

![detalle2](capturas/citaInfo2.png)

Funcionalidades:
- Listar citas con paginacion
- Buscar por paciente, medico o DNI
- Filtrar por estado y rango de fechas
- Crear cita (paciente + medico + fecha + consultorio)
- Ver detalle completo de la cita
- Cancelar cita
- Completar cita (marcar como atendida)
- Reprogramar cita
- Marcar como "no asistio"
- Eliminar cita

---

## 7. Gestion de Horarios

Administracion de horarios por medico.

![horarios](capturas/horarioMedico.png)

Funcionalidades:
- Listar horarios por medico
- Agregar horario (dia, hora inicio, hora fin, consultorio)
- Editar horario
- Activar / Desactivar horario

---

## Resumen de Funcionalidades

| Modulo | Estado |
|--------|--------|
| Landing Page | Completo |
| Login / Autenticacion | JWT + roles |
| Dashboard | Metricas completas |
| Pacientes CRUD | Completo |
| Medicos CRUD | Completo |
| Citas CRUD | Completo (+ estados) |
| Horarios | Completo |
| Seguridad | Roles ADMIN / MEDICO / RECEPCIONISTA |
| Backend API | Spring Boot + JPA + MySQL |
| Frontend | Angular + PrimeNG + Tailwind |
