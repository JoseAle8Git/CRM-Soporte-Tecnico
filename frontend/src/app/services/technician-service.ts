import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { TechnicianIncidence } from '../models/technician-incidence.interface';
import { TechnicianPersonalStats } from '../models/tech-stats.interface';

@Injectable({
  providedIn: 'root'
})
export class TechnicianService {

  private http = inject(HttpClient);
  private baseUrl = '/api/technician';

  getMyIncidences(): Observable<TechnicianIncidence[]> {
    return this.http.get<TechnicianIncidence[]>(`${this.baseUrl}/incidences`);
  }

  updateStatus(incidenceId: number, status: string): Observable<TechnicianIncidence> {
    return this.http.patch<TechnicianIncidence>(
      `${this.baseUrl}/incidences/${incidenceId}/status`,
      { status }   // <-- ahora enviamos JSON en el body
    );
  }


  getMyStats(): Observable<TechnicianPersonalStats> {
    return this.http.get<TechnicianPersonalStats>(`${this.baseUrl}/stats`);
  }
}
