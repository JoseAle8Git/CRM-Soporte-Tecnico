import { inject } from '@angular/core';
import { ActivatedRouteSnapshot, CanActivateFn, Router, RouterStateSnapshot } from '@angular/router';
import { Auth } from './auth';

const ROLE_REDIRECTS: { [key: string]: string} = {
  'ADMIN': '/protected/admin',
  'MANAGER': 'protected/manager',
  'TECH': '/protected/tech',
  'CLIENT': '/protected/client'
}

export const authGuard: CanActivateFn = (route: ActivatedRouteSnapshot, state: RouterStateSnapshot) => {
  const authService = inject(Auth);
  const router = inject(Router);
  if(!authService.isLoggedIn()) {
    console.warn("Acceso denegado. Redirigiendo a Login.");
    return router.createUrlTree(['/auth/login']);
  }

  const userRole = authService.getUserRole()?.toUpperCase();
  const targetSegment = ROLE_REDIRECTS[userRole || ''];

  if(!targetSegment) {
    console.error("Acceso denegado. Rol de usuario desconocido.");
    return router.createUrlTree(['/auth/login']);
  }

  if(state.url === '/protected') {
    return router.createUrlTree([targetSegment]);
  }

  return true;

};
