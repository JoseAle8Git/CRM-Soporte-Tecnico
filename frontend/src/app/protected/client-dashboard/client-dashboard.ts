import { Component, inject, OnInit, signal } from '@angular/core';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatDividerModule } from '@angular/material/divider';
import { Auth } from '../../auth/auth';

@Component({
  selector: 'app-client-dashboard',
  standalone: true,
  imports: [MatCardModule, MatButtonModule, MatIconModule, MatDividerModule],
  templateUrl: './client-dashboard.html',
  styleUrl: './client-dashboard.css',
})
export class ClientDashboard implements OnInit {

  // 1. Inyectamos tu servicio
  private authService = inject(Auth);

  // 2. Signal inicializada vacía o con un texto de carga
  userName = signal<string>('Cargando...');
  companyData = signal<any>(null);
  chartData = signal<any>(null);
  tickets = signal<any[]>([]);


  ngOnInit() {
    // 3. Llamamos a tu nuevo método
    const userInDb = this.authService.getUserData();

    if (userInDb) {
      // Convertimos a string primitivo por si acaso y actualizamos la señal
      this.userName.set(userInDb.toString());
    } else {
      // Opcional: Si devuelve null, podrías redirigir al login o poner "Invitado"
      this.userName.set('Invitado');
    }
  }
} 