import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { UserCreationRequest } from '../models/user-creation.interface';
import { Observable } from 'rxjs';
import { UserBasic } from '../models/user-basic.interface';
import { ClientInfo } from '../models/client-dashboard.interface';

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
    return this.http.get<UserBasic[]>(`${USER_API_URL}/basic-list`, { withCredentials: true });
  }

  /**
   * Obtiene un usuario para el formulario de edición.
   * @param userId 
   * @returns 
   */
  getUserById(userId: number): Observable<UserCreationRequest> {
    return this.http.get<UserCreationRequest>(`${USER_API_URL}/${userId}`, { withCredentials: true});
  }

  /**
   * Envías los datos modificados del usuario. Protegido por JWT.
   * @param userId 
   * @param request 
   * @returns 
   */
  updateUser(userId: number, request: UserCreationRequest): Observable<UserBasic> {
    return this.http.put<UserBasic>(`${USER_API_URL}/${userId}`, request, { withCredentials: true });
  }

  deleteUser(userId: number): Observable<void> {
    return this.http.delete<void>(`${USER_API_URL}/${userId}`, { withCredentials: true });
  }

  getAllClients(activeFilter?: boolean, packageFilter?: string): Observable<ClientInfo[]> {
    let params = new HttpParams();
    if(activeFilter !== undefined) {
      params = params.set('activeFilter', activeFilter.toString());
    }
    if(packageFilter) {
      params = params.set('packageFilter', packageFilter);
    }

    return this.http.get<ClientInfo[]>(`${USER_API_URL}/clients-list`, { withCredentials: true });
  }

}
