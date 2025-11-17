import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { Observable, tap } from 'rxjs';

const API_URL = 'http://localhost:8080/sistema/api/v1/auth';

export interface LoginRequest {
  username: string;
  password: string;
}

export interface AuthResponse {
  userId: number;
  username: string;
  role: string;
}

@Injectable({
  providedIn: 'root',
})
export class Auth {
  private currentUser: AuthResponse | null = null;

  private readonly USER_KEY = 'currentUser';

  constructor(
    private http: HttpClient,
    private router: Router
  ) {
    this.loadUserFromStorage();
  }

  login(request: LoginRequest): Observable<AuthResponse> {
    return this.http.post<AuthResponse>(`${API_URL}/login`, request, { withCredentials: true }).pipe(
      tap(response => {
        this.setAuth(response);
      })
    )
  }

  setAuth(respose: AuthResponse): void {
    this.currentUser = respose;
    localStorage.setItem(this.USER_KEY, JSON.stringify(respose));
    this.router.navigate(['/protected']);
  }

  isLoggedIn(): boolean {
    return !!this.currentUser;
  }

  private loadUserFromStorage(): void {
    const userJson = localStorage.getItem(this.USER_KEY);
    if (userJson) {
      this.currentUser = JSON.parse(userJson) as AuthResponse;
    }
  }

  logout(): void {
    this.http.post(`${API_URL}/logout`, {}, { withCredentials: true }).subscribe({
      next: () => {
        this.currentUser = null;
        localStorage.removeItem(this.USER_KEY);
        this.router.navigate(['/auth/login']);
      },
      error: (err) => {
        console.error("Error al hacer logout, forzando cierre local.");
        this.currentUser = null;
        localStorage.removeItem(this.USER_KEY);
        this.router.navigate(['/auth/login']);
      }
    })
  }

  getUserRole(): string | null {
    return this.currentUser ? this.currentUser.role : null;
  }

  getUserData(): string | null {
    return this.currentUser ? this.currentUser.username : null;
  }

}
