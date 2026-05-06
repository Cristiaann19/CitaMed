# CitaMed

This project was generated using [Angular CLI](https://github.com/angular/angular-cli) version 21.2.1.

## Development server

To start a local development server, run:

```bash
ng serve
```

## Estructura del proyecto
```
src/
└── app/
    ├── core/                       # Lógica global (Servicios y Guards)
    │   ├── guards/                 # auth.guard.ts
    │   └── services/               # loader.service.ts, auth.service.ts
    │
    ├── shared/                     # Elementos reutilizables en TODA la app
    │   ├── components/             # Botones, Spinners, Alertas
    │   ├── interfaces/             # producto.interface.ts, user.interface.ts
    │   └── pipes/                  # Pipes personalizados
    │
    ├── layout/                     # El "Esqueleto" de tu Dashboard
    │   ├── dashboard-layout/       # Sidebar + Header + RouterOutlet
    │   └── components/             # SidebarComponent, HeaderAdminComponent
    │
    ├── features/                   # Tus módulos de negocio (Páginas)
    │   ├── home/                   # Landing page (Inicio, Servicios, etc.)
    │   │   ├── components/         # NavbarComponent, FooterComponent (públicos)
    │   │   └── home.component.ts
    │   ├── auth/                   # Módulo de acceso
    │   │   └── login/              # login-component.ts
    │   └── admin/                  # Vistas internas del Dashboard
    │       ├── productos/          # Gestion de productos del Dashboard
    │       └── servicios/          # Gestión de servicios del Dashboard
    │
    ├── app.routes.ts               # Configuración de navegación
    ├── app.component.ts            # Componente raíz
    └── app.config.ts               # Proveedores globales (HttpClient, etc.)
```