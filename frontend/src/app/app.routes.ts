import { Routes } from '@angular/router';
import { Store } from './store/store';
import { authGuard } from './auth/auth-guard';
import { Login } from './auth/login/login';

export const routes: Routes = [
    // Ruta pública: Tienda.
    {
        path: 'store',
        component: Store
    },
    {
        path: 'auth/login',
        component: Login
    },
    // Ruta genérica para el dashbooard.
    {
        path: 'dashboard',
        canActivate: [authGuard],
        loadChildren: () => import('./main/main-module').then(m => m.MainModule)
    },
    // Ruta de Autenticación (carga perezosa). Contiene el Login.
    {
        path: 'auth',
        loadChildren: () => import('./auth/auth-module').then(m => m.AuthModule)
    },
    // Ruta por defecto/raíz.
    {
        path: '',
        component: Login,
        pathMatch: 'full'
    }
];
