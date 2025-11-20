import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { AssignmentRequest, IncidenceDashboard } from '../models/incidence-dashboard.interface';

const INCIDENCE_API_URL = 'http://localhost:8080/sistema/api/v1/incidences';

@Injectable({
  providedIn: 'root',
})
export class IncidenceService {
  
  constructor(private http: HttpClient) { }

  /**
   * Obtiene la lista de incidencias para la tabla del manager.
   * @returns 
   */
  findAllIncidencesForDashboard(): Observable<IncidenceDashboard[]> {
    return this.http.get<IncidenceDashboard[]>(`${INCIDENCE_API_URL}/dashboard-list`, { withCredentials: true });
  }

  /**
   * Asigna la incidencia a un técnico, activando la notificación asíncrona.
   * @param request 
   * @returns 
   */
  assignTechnician(request: AssignmentRequest): Observable<IncidenceDashboard> {
    return this.http.post<IncidenceDashboard>(`${INCIDENCE_API_URL}/assign`, request, { withCredentials: true });
  }


}
