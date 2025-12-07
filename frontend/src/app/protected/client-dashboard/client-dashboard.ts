import { Component, inject, OnInit, signal } from '@angular/core';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatDividerModule } from '@angular/material/divider';
import { MatDialog } from '@angular/material/dialog';
import { DatePipe } from '@angular/common';
import { BaseChartDirective } from 'ng2-charts';
import { ChartConfiguration, ChartData, ChartType } from 'chart.js';
import { IncidenceService } from '../../services/incidence-service';
import { Auth } from '../../auth/auth';
import { ClientService, ClientData } from '../../services/client';
import { CompanyModalComponent } from './components/company-modal/company-modal';
import { CreateIncidenceModalComponent } from './components/create-incidence-modal/create-incidence-modal';



@Component({
  selector: 'app-client-dashboard',
  standalone: true,
  imports: [MatCardModule, MatButtonModule, MatIconModule, MatDividerModule, DatePipe, BaseChartDirective],
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
  chartData = signal<ChartData<'bar'> | null>(null);

  // aqui se guardan los tickets
  tickets = signal<any[]>([]);

  // config del gráfico
  public barChartOptions: ChartConfiguration['options'] = {
    responsive: true,
    maintainAspectRatio: false, // pa q se ajuste bien
    plugins: {
      legend: { display: false } 
    }
  };
  public barChartType: ChartType = 'bar';

  ngOnInit() {
    // carga usuario
    const userInDb = this.authService.getUserData();
    if (userInDb) this.userName.set(userInDb.toString());

    // carga empresa y sub clientes
    const myClientId = this.authService.currentUserId;

    if (myClientId) {
      this.clientService.getClientById(myClientId).subscribe({
        next: (data) => {
          this.companyData.set(data);
          this.loadChartData(data.id);
        },
        error: (e) => console.error('Error cargando empresa:', e)
      });
    }

    // cargar tickets
    const myId = this.authService.currentUserId;
    if (myId) {
      this.loadTickets(myId);
    }
  }

  //grafico logica
  loadChartData(companyId: number) {
    this.clientService.getSubClients(companyId).subscribe({
      next: (subClients: any[]) => {
        // 1. Mapeamos los nombres usando 'company_name'
        const nombres = subClients.map(c => c.company_name);
        // 2. Mapeamos la facturación usando 'billing'
        const facturacion = subClients.map(c => c.billing);

        this.chartData.set({
          labels: nombres,
          datasets: [
            {
              data: facturacion,
              label: 'Facturación (€)',
              backgroundColor: ['#3f51b5', '#ff4081', '#4caf50', '#ff9800'], 
              borderRadius: 5
            }
          ]
        });
      },
      error: (e) => console.error('Error cargando gráfico:', e)
    });
  }

  // Función que pide los datos al Back
  loadTickets(userId: number) {
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

    //cuando se cierra, recarga tickets
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