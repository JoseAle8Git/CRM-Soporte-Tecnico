import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { TechIncidenceStats } from '../models/tech-stats.interface';

const STATS_API_URL = 'http://localhost:8080/sistema/api/v1/stats';

@Injectable({
  providedIn: 'root',
})
export class StatsService {
  
  constructor(private http: HttpClient) { }

  /**
   * Obtiene los datos para la gráfica de rendimiento. Protegido por JWT.
   * @returns 
   */
  getIncidenceCountsByTechnician(): Observable<TechIncidenceStats[]> {
    // Enpoint: /stats/incidences-by-tech.
    return this.http.get<TechIncidenceStats[]>(`${STATS_API_URL}/incidences-by-tech`, { withCredentials: true });
  }

}
