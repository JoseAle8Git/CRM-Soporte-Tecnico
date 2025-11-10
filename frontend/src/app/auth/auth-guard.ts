import { inject } from '@angular/core';
import { ActivatedRouteSnapshot, CanActivateFn, Router, RouterStateSnapshot } from '@angular/router';
import { Auth } from './auth';

export const authGuard: CanActivateFn = (route: ActivatedRouteSnapshot, state: RouterStateSnapshot) => {
  const authService = inject(Auth);
  const router = inject(Router);
  if(authService.isLoggedIn()) {
    return true;
  } else {
    console.warn("Acceso denegado. Redirigiendo a Login.");
    return router.createUrlTree(['/auth/login']);
  }
};
