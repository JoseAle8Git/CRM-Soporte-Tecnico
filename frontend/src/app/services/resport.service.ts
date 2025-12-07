import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { ReportLog } from '../models/report-log.interface';

const REPORT_API_URL = 'http://localhost:8080/sistema/api/v1/reports';

@Injectable({
  providedIn: 'root',
})
export class ResportService {

  constructor(private http: HttpClient) {}

  /**
   * Obtiene el log de reportes para la tabla.
   * @returns 
   */
  getReportLogs(): Observable<ReportLog[]> {
    return this.http.get<ReportLog[]>(`${REPORT_API_URL}/logs`, { withCredentials: true });
  }

  /**
   * Contruye la URL de descarga.
   * @param reportId 
   * @returns 
   */
  getDownloadUrl(reportId: number): string {
    // La descarga debe hacerse fuera de la suscripción para que el navegador maneje el archivo.
    return `${REPORT_API_URL}/${reportId}/download`;
  }
  
}
