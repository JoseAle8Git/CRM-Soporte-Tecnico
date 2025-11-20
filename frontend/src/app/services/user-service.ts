import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { UserCreationRequest } from '../models/user-creation.interface';
import { Observable } from 'rxjs';
import { UserBasic } from '../models/user-basic.interface';

// URL base para el controlador de usuarios.
const USER_API_URL = 'http://localhost:8080/sistema/api/v1/users';

@Injectable({
  providedIn: 'root',
})
export class UserService {
  
  constructor(private http: HttpClient) { }

  /**
   * Crea un nuevo usuario.Protegido por JWT.
   * @param request 
   * @returns 
   */
  createNewUser(request: UserCreationRequest): Observable<UserBasic> {
    // La cookie HttpOnly con el JWT se adjunta automáticamente con la solicitud.
    return this.http.post<UserBasic>(USER_API_URL, request, { withCredentials: true });
  }

  /**
   * Obtiene la lista de usuarios para la tabla del Dashboard. Protegido por JWT.
   * @returns 
   */
  getAllBasicUsers(): Observable<UserBasic[]> {
    // Endpoint: /users/basic-list.
    return this.http.get<UserBasic[]>(`%{USER_API_URL}/basic-list`, { withCredentials: true });
  }

}
