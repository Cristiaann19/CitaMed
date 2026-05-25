import { CanActivateFn, Router } from '@angular/router';
import { inject } from '@angular/core';
import { AuthService } from '../services/auth-service';

export const roleGuard: CanActivateFn = (route, state) => {
  const auth = inject(AuthService);
  const router = inject(Router);

  if (!auth.isLoggedIn()) {
    router.navigate(['/login']);
    return false;
  }

  const allowed: string[] = route.data?.['roles'] ?? [];
  if (!allowed.length) return true;

  const userRoles = auth.getRolesFromToken();
  const ok = userRoles.some((r) => allowed.includes(r) || allowed.includes('ROLE_' + r));
  if (!ok) {
    router.navigate(['/inicio']);
  }
  return ok;
};
