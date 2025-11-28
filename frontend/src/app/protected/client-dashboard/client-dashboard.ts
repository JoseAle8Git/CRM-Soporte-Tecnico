import { Component, inject, OnInit, signal } from '@angular/core';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatDividerModule } from '@angular/material/divider';
import { MatDialog } from '@angular/material/dialog';
import { DatePipe } from '@angular/common';

// TUS IMPORTS
import { IncidenceService } from '../../services/incidence-service';
import { Auth } from '../../auth/auth';
import { ClientService, ClientData } from '../../services/client';
import { CompanyModalComponent } from './components/company-modal/company-modal';
import { CreateIncidenceModalComponent } from './components/create-incidence-modal/create-incidence-modal';

@Component({
  selector: 'app-client-dashboard',
  standalone: true,
  imports: [MatCardModule, MatButtonModule, MatIconModule, MatDividerModule, DatePipe],
  templateUrl: './client-dashboard.html',
  styleUrl: './client-dashboard.css',
})
export class ClientDashboard implements OnInit {

  private authService = inject(Auth);
  private clientService = inject(ClientService);
  private dialog = inject(MatDialog);
  private incidenceService = inject(IncidenceService);

  // SIGNALS
  userName = signal<string>('Cargando...');
  companyData = signal<ClientData | null>(null);
  chartData = signal<any>(null);

  // AQUÍ SE GUARDAN TUS TICKETS
  tickets = signal<any[]>([]);

  ngOnInit() {
    // 1. Cargar Usuario
    const userInDb = this.authService.getUserData();
    if (userInDb) this.userName.set(userInDb.toString());

    // 2. Cargar Empresa
    // Usamos el ID del usuario logueado (que es 4)
    const myClientId = this.authService.currentUserId; // ✅ Correcto

    if (myClientId) {
      this.clientService.getClientById(myClientId).subscribe({
        next: (data) => {
          console.log('Empresa cargada:', data); // Para verificar en consola
          this.companyData.set(data);
        },
        error: (e) => console.error('Error cargando empresa:', e)
      });
    }

    // 3. CARGAR TICKETS (Lo importante ahora)
    const myId = this.authService.currentUserId;
    if (myId) {
      this.loadTickets(myId);
    }
  }

  // Función que pide los datos al Back
  loadTickets(userId: number) {
    console.log('🔎 Buscando tickets para el usuario ID:', userId); // <--- Chivato

    this.incidenceService.getIncidencesByClient(userId).subscribe({
      next: (data) => {
        console.log('✅ Tickets encontrados:', data.length);
        this.tickets.set(data);
      },
      error: (err) => console.error('❌ Error:', err)
    });
  }

  // Abrir Modal de Crear Ticket
  openCreateTicket() {
    const dialogRef = this.dialog.open(CreateIncidenceModalComponent, {
      width: '600px'
    });

    // AL CERRAR EL MODAL, REFRESCAMOS LA LISTA
    dialogRef.afterClosed().subscribe(result => {
      if (result === true) {
        const myId = this.authService.currentUserId;
        if (myId) this.loadTickets(myId);
      }
    });
  }

  // Abrir Modal de Empresa
  openDetails() {
    const company = this.companyData();
    if (!company) return;

    this.clientService.getUsersByClientId(company.id).subscribe(users => {
      this.clientService.getSubClients(company.id).subscribe(subClients => {
        this.dialog.open(CompanyModalComponent, {
          width: '900px',
          data: { company, users, subClients }
        });
      });
    });
  }
}