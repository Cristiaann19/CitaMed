# Observaciones Finales - CitaMed

Análisis de cumplimiento de requisitos funcionales mínimos.

---

## Backend (Spring Boot)

| # | Requisito | Cumple | Observaciones |
|---|-----------|--------|---------------|
| 1 | Arquitectura en capas (Controller, Service, Repository) | ✅ Sí | Separación clara en 3 capas |
| 2 | Spring Data JPA (Hibernate) | ✅ Sí | Repositorios extienden `JpaRepository`, anotaciones `@Entity` |
| 3 | CRUD: Pacientes, Médicos, Citas | ✅ Sí | Los 3 tienen CRUD completo (crear, leer, actualizar, eliminar) |
| 4 | JPQL en ≥2 consultas personalizadas | ✅ Sí | Múltiples JPQL: `buscarConFiltros`, `ultimasCitas`, `citasPorEspecialidad`, `medicosActivos`, `ingresos` |
| 5 | `@Transactional` | ⚠️ Parcial | Presente en `CitaService`, `MedicoService`, `ReservaService`, pero **ausente** en `PacienteService.save()` (crea Paciente + HistorialMedico), `MedicoService.save()` (crea Usuario + Medico), `EmpleadoService.save()`, `UsuarioService.saveUser()`, `PagoService.save()` |
| 6 | Validaciones Jakarta | ⚠️ Parcial | Anotaciones `@NotBlank`, `@Pattern`, `@Email`, etc. **sí** están en las entidades, pero `@Valid` en los controladores solo está en `PacienteController`. Falta en `CitaController`, `MedicoController`, `UsuarioController`, etc. |
| 7 | Spring Security (login, roles ADMIN/MEDICO, autorización) | ✅ Sí | JWT, roles ADMIN/MEDICO/RECEPCIONISTA/ENFERMERO, autorización por endpoint. Sin embargo, `MEDICO` puede acceder a **todos** los pacientes/citas (no hay filtro automático por médico logueado) |
| 8 | API REST con CORS | ✅ Sí | Configuración global y `@CrossOrigin` en controladores (aunque usar `origins="*"` en 12 controladores es más permisivo que la config global) |

---

## Frontend (Angular)

| # | Requisito | Cumple | Observaciones |
|---|-----------|--------|---------------|
| 9 | Estructura modular | ✅ Sí | `core/`, `features/`, `layout/`, `shared/`, `model/` |
| 10 | Servicios API REST | ✅ Sí | `auth-service`, `cita-service`, `paciente-service`, `medico-service`, etc. |
| 11a | Login | ✅ Sí | Implementado con ReactiveForms y validación |
| 11b | Dashboard principal | ✅ Sí | Tarjetas de estadísticas, agenda hoy, médicos activos, últimas citas |
| 11c | Gestión de Pacientes | ✅ Sí | CRUD completo con paginación y búsqueda |
| 11d | Gestión de Médicos | ✅ Sí | CRUD completo |
| 11e | Gestión de Citas | ✅ Sí | CRUD + cancelar, completar, reprogramar, no-asistió |
| 12 | Formularios con validaciones | ✅ Sí | ReactiveForms con validadores |
| 13 | Guards (protección de rutas) | ✅ Sí | `auth.guard` y `role.guard` |
| 14 | Diseño funcional | ✅ Sí | PrimeNG + TailwindCSS |
| 15 | CRUD completo desde UI | ⚠️ Parcial | Pacientes, Médicos, Citas ✅ COMPLETO. Especialidades solo lectura. Usuarios, Empleados, Pagos, Reportes, Historial Médico ❌ **son stubs vacíos** (sin template) |

---

## Módulo mínimo

| # | Requisito | Cumple | Observaciones |
|---|-----------|--------|---------------|
| 16 | Pacientes CRUD | ✅ Sí | Registrar, listar, editar, eliminar (soft-delete) |
| 17 | Médicos CRUD | ✅ Sí | Registrar, listar, editar, cambiar estado |
| 18 | Citas CRUD + disponibilidad | ✅ Sí | Crear (paciente+médico+fecha), listar, cancelar, actualizar, reprogramar. Validación de disponibilidad: evita solapamiento de horarios |

---

## Seguridad

| # | Requisito | Cumple | Observaciones |
|---|-----------|--------|---------------|
| 19 | Login obligatorio | ✅ Sí | Auth guard en todas las rutas `/admin` |
| 20 | Autenticación Spring Security + protección endpoints | ✅ Sí | JWT filter, endpoints protegidos |
| 21 | Autorización: ADMIN (todo), MEDICO (solo sus citas/pacientes) | ⚠️ Parcial | ADMIN y MEDICO existen, pero **MEDICO puede ver TODOS los pacientes y citas** vía endpoints genéricos. No hay filtro automático que limite al médico logueado a solo sus datos |
| 22 | JWT | ✅ Sí | Token JWT con roles, almacenado en localStorage, enviado vía interceptor |

---

## Dashboard

| # | Requisito | Cumple | Observaciones |
|---|-----------|--------|---------------|
| 23 | Total de pacientes | ❌ No | Muestra `pacientesActivos` (pacientes distintos del mes), **no** el total general |
| 24 | Total de médicos | ❌ No | Muestra médicos con más citas (top doctors), **no** el total de médicos registrados |
| 25 | Total de citas programadas | ❌ No | Muestra `citasHoy`, **no** el total de citas programadas |
| 26 | Citas del día | ✅ Sí | `citasHoy` + agenda detallada del día |
| 27 | Accesos rápidos a módulos | ✅ Sí | Sidebar con navegación a todos los módulos |

---

## Técnicos

| # | Requisito | Cumple | Observaciones |
|---|-----------|--------|---------------|
| 28 | Spring Boot 3.x | ✅ Sí | v4.0.5 |
| 29 | MySQL / PostgreSQL | ✅ Sí | MySQL |
| 30 | Hibernate (JPA) | ✅ Sí | Via Spring Data JPA |
| 31 | Angular 15+ | ✅ Sí | v21.2 |
| 32 | Node.js | ✅ Sí | Presupuesto (proyecto funcional) |
| 33 | API REST | ✅ Sí | Endpoints RESTful |
| 34 | Arquitectura cliente-servidor | ✅ Sí | Backend + Frontend separados |
| 35 | Manejo correcto de excepciones | ❌ No | Solo `RuntimeException` genérico. Sin excepciones personalizadas, sin handler para `MethodArgumentNotValidException`, `AccessDeniedException`, `DataIntegrityViolationException`. Sin DTO de error estructurado |

---

## Resumen

| Estado | Cantidad |
|--------|----------|
| ✅ Cumple completamente | **22** |
| ⚠️ Cumple parcialmente | **4** |
| ❌ No cumple | **4** |

### Pendientes críticos

1. **Dashboard**: Agregar total general de pacientes, médicos y citas programadas (no solo datos del mes/día).
2. **Manejo de excepciones**: Implementar `@ExceptionHandler` para `MethodArgumentNotValidException`, `AccessDeniedException`, `DataIntegrityViolationException`, y crear un DTO de error estructurado.
3. **`@Transactional`**: Agregar en servicios que realizan múltiples operaciones de escritura (`PacienteService.save()`, `MedicoService.save()`, etc.).
4. **`@Valid`**: Agregar en todos los controladores que reciben `@RequestBody` para activar las validaciones de Jakarta.
5. **Autorización MEDICO**: Implementar filtro para que un médico solo vea sus propias citas/pacientes asignados.
6. **Componentes frontend stub**: Completar los componentes de Usuarios, Empleados, Pagos, Reportes e Historial Médico que actualmente están vacíos.
7. **CORS**: Reemplazar `@CrossOrigin(origins = "*")` por la configuración global más restrictiva.
