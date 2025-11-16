import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { ServiceRequest } from '../models/service-request';

const API_URL = 'http://localhost:8080/sistema/api/v1/public';

@Injectable({
  providedIn: 'root',
})
export class PublicSevice {
  
  constructor(private http: HttpClient) {}

  sendServiceRequest(request: ServiceRequest): Observable<string> {
    return this.http.post(`${API_URL}/service-request`, request, { responseType: 'text' });
  }

}
