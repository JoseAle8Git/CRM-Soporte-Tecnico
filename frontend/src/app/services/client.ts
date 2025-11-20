import { HttpClient } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';

// --- INTERFACES modales ---

// 1. la Empresa (Tabla 'client')
export interface ClientData {
  id: number;
  companyName: string;
  cif: string;
  direction: string;
  active: boolean;
  servicePackage: string;
}

// 2. los Compañeros (Tabla 'app_user')
export interface CompanyUser {
  username: string;
  name: string;
  email: string;
  role: string;
}

// 3. los Sub-Clientes (Tabla 'clients_of_clients')
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

  // B. Obtener compañeros
  getUsersByClientId(clientId: number): Observable<CompanyUser[]> {
    return this.http.get<CompanyUser[]>(`${this.apiUrl}/${clientId}/users`, { withCredentials: true });
  }

  // C. Obtener sub-clientes (facturación)
  getSubClients(clientId: number): Observable<SubClient[]> {
    // Asegúrate de tener este endpoint en Java (lo hicimos antes: /{id}/sub-clients)
    return this.http.get<SubClient[]>(`${this.apiUrl}/${clientId}/sub-clients`, { withCredentials: true });
  }
}