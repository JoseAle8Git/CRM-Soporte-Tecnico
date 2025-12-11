import { HttpClient } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';


// 1. la Empresa client 
export interface ClientData {
  id: number;
  companyName: string;
  cif: string;
  direction: string;
  active: boolean;
  servicePackage: string;
}

// La información del usuario en contexto
export interface UserContext {
  userId: number;
  username: string;
  role: string;
  clientId: number;
  companyName: string;
}

// 2. los registrados para log
export interface CompanyUser {
  username: string;
  name: string;
  email: string;
  role: string;
}

// 3. clients of clients
export interface SubClient {
  id: number;
  name: string;
  active: boolean;
  billing: number;
}

@Injectable({
  providedIn: 'root'
})
export class ClientService {
  private http = inject(HttpClient);

  // Ajusta el puerto si tu Spring Boot usa otro
  private apiUrl = 'http://localhost:8080/sistema/api/v1/clients';

  // A. Obtener datos de la empresa
  getClientById(id: number): Observable<ClientData> {
    return this.http.get<ClientData>(`${this.apiUrl}/${id}`, { withCredentials: true });
  }

  // B. Obtener usuario de la empresa
  getUsersByClientId(clientId: number): Observable<CompanyUser[]> {
    return this.http.get<CompanyUser[]>(`${this.apiUrl}/${clientId}/users`, { withCredentials: true });
  }

  // C. Obtener clientsofclients
  getSubClients(clientId: number): Observable<SubClient[]> {
    //este endpoint en Java  
    return this.http.get<SubClient[]>(`${this.apiUrl}/${clientId}/sub-clients`, { withCredentials: true });
  }

  // Pedir mi perfil usando el ClientController
  getMyProfile(): Observable<UserContext> {
    // Llamamos a /clients/profile
    return this.http.get<UserContext>(`${this.apiUrl}/profile`, { withCredentials: true });
  }

}