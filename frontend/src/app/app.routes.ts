import { RouterModule, Routes } from '@angular/router';
import { NgModule } from '@angular/core';

// Definición de las rutas principales.
const routes: Routes = [
    // 1. Rita de autenticación. (Carga perezosa)
    // Cuando el usuario va a 'localhost:4200/auth', carga el modulo Auth.
    {
        path: 'auth',
        loadChildren: () => import('./auth/auth-module').then(m => m.AuthModule)
    },
    // 2. Ruta por defecto/raíz.
    // Redirige la URL raíz ('/') a la ruta de login.
    {
        path: '',
        redirectTo: 'auth/login',
        pathMatch: 'full'
    }
];

@NgModule({
    imports: [RouterModule.forRoot(routes)],
    exports: [RouterModule]
})
export class AppRoutingModule {}
