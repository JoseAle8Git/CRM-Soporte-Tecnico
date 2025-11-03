import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { Observable, tap } from 'rxjs';

/**
 * Interfaz para definir la estructura de la respuesta de login.
 * Define la estructura de los datos que esperamos del Backend.
 */
interface AuthResponse {
  userId: number;
  username: string;
  role: string;
}

@Injectable({
  providedIn: 'root',
})
export class Auth {

  // URL base del Backend, asumiendo que Spring Boot se ejecuta en el puerto 8080
  private apiUrl = 'http://localhost:8080/api/auth';

  // Variable para almacenar los datos del usuario logueado (solo información no sensible).
  private currentUser: AuthResponse | null = null;

  // 1. Constructor: Inyección de dependencias.
  constructor(
    private http: HttpClient, // Necesario para las llamadas http asíncronas.
    private router: Router // Necesario para la navegación.
  ) {
    // Al inicializar, podríamos intentar cargar datos de usaurio si se almacenaran localmente (no el token).
    this.loadCurrentUser();
  }

  /**
   * 2. Método de login: Envía credenciales al Backend.
   * Recibe las credenciales, devuelve un Observable (asincronía).
   */
  login(credentials: any) {
    // La petición es asíncrona (devuelve un Observable) para no bloquear la interfaz.
    // La opción { withCredentials: true } permite que el navegador adjunte las cookies (incluidas la HttpOnly) a la petición y la reciba en la respuesta.
    return this.http.post<AuthResponse>(this.apiUrl + 'login', credentials, { withCredentials: true })
      .pipe(
        tap(response => {
          this.setAuth(response);
        })
      );
    // Nota: La interfaz AuthResponse ahora solo esperará la info del ususario (no el token).
  }

  /**
   * 3. Método para gestionar el estado después del login exitoso.
   */
  setAuth(response: AuthResponse): void {
    // Guardamos la información no sensible para mosatrarla en la interfaz.
    this.currentUser = response;
    sessionStorage.setItem('user_info', JSON.stringify(response));

    // Redirige al usuario al Dashboard principal.
    this.router.navigate(['/dashboard']);
  }

  /**
   * 4. Método para cargar datos del usuario desde SessionStorage al iniciar el servicio.
   */
  loadCurrentUser(): void {
    const userInfo = sessionStorage.getItem('user_info');
    if(userInfo) {
      this.currentUser = JSON.parse(userInfo);
    }
  }

  /**
   * 5. Método de logout
   */
  logout(): void {
    // Llama a un endpoint de logout en el Backend para invalidar el JWT en la Cookie.
    this.http.post(this.apiUrl + 'logout', {}, { withCredentials: true }).subscribe({
      next: () => {
        // Limpia el estado local y la sesión.
        this.currentUser = null;
        sessionStorage.removeItem('user_info');
        // Redirige al login.
        this.router.navigate(['/auth/login']);
      },
      // Si hay un error, igualmente forzamos el logout local.
      error: () => {
        this.currentUser = null;
        sessionStorage.removeItem('user_info');
        this.router.navigate(['/auth/login']);
      }
    })
  }

  /**
   * 6. Getter para obtener el estado.
   */
  getIsAutheticated(): boolean {
    return !!this.currentUser;
  }

  /**
   * Getter para obtener el rol del usuario (Necesario para la autorizacción en el Frontend).
   */
  getUserRole(): string | undefined {
    return this.currentUser?.role;
  }

}
