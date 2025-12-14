import { Component, inject, OnInit, signal } from '@angular/core';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatDividerModule } from '@angular/material/divider';
import { MatDialog } from '@angular/material/dialog';
import { DatePipe } from '@angular/common';
import { BaseChartDirective } from 'ng2-charts';
import { ChartConfiguration, ChartData, ChartType } from 'chart.js';

// TUS IMPORTS
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

  // --- CONFIG GRÁFICO ---
  public barChartOptions: ChartConfiguration['options'] = {
    responsive: true,
    maintainAspectRatio: false,
    plugins: {
      legend: { display: true }
    }
  };
  public barChartType: ChartType = 'bar';

  public chartData = signal<ChartData<'bar'>>({
    labels: [],
    datasets: [{ data: [], label: 'Facturación (€)' }]
  });

  hasChartData = signal<boolean>(false);

  // --- SIGNALS DATOS ---
  userName = signal<string>('Cargando...');
  companyData = signal<ClientData | null>(null);
  tickets = signal<any[]>([]);

  ngOnInit() {

    // 1. PEDIR DATOS AL ENDPOINT DEL CLIENT SERVICE
    this.clientService.getMyProfile().subscribe({
      next: (user) => {
        this.userName.set(user.username);
        const myCompanyId = user.clientId;
        const myUserId = user.userId;

        // 2. CARGAR EMPRESA Y GRÁFICOS
        if (myCompanyId) {
          this.clientService.getClientById(myCompanyId).subscribe(data => {
            this.companyData.set(data);
            this.loadChartMetrics(myCompanyId);
          });

          // Tickets de la empresa
          this.loadTickets(myCompanyId);
        }
      },
    });
  }

  // --- FUNCIÓN PARA CARGAR TICKETS (Por ID de Empresa) ---
  loadTickets(companyId: number) {
    this.incidenceService.getIncidencesByClient(companyId).subscribe({
      next: (data) => {
        this.tickets.set(data);
      },
    });
  }

  // --- FUNCIÓN GRÁFICO ---
  loadChartMetrics(companyId: number) {
    this.clientService.getSubClients(companyId).subscribe({
      next: (subClients: any[]) => {
        const nombres = subClients.map(c => c.company_name || c.name); // Aseguramos compatibilidad de nombres
        const dinero = subClients.map(c => c.billing);

        this.chartData.set({
          labels: nombres,
          datasets: [{
            data: dinero,
            label: 'Facturación (€)',
            backgroundColor: ['#3f51b5', '#ff4081', '#4caf50', '#ff9800'],
            hoverBackgroundColor: '#512da8'
          }]
        });

        if (subClients.length > 0) {
          this.hasChartData.set(true);
        }
      },
    });
  }

  // --- MODAL CREAR TICKET ---
  openCreateTicket() {
    const dialogRef = this.dialog.open(CreateIncidenceModalComponent, {
      width: '600px'
    });

    // AL CERRAR: Recargamos usando el ID DE EMPRESA
    dialogRef.afterClosed().subscribe(result => {
      if (result === true) {
        const myCompanyId = Number(this.authService.getClientId()); // <--- ¡CORREGIDO AQUÍ!
        if (myCompanyId) {
          this.loadTickets(myCompanyId);
        }
      }
    });
  }

  openDetails() {
    const company = this.companyData(); // Obtenemos la empresa actual
    if (!company) return;

    // Pedimos datos frescos para abrir el modal
    this.clientService.getUsersByClientId(company.id).subscribe(users => {
      this.clientService.getSubClients(company.id).subscribe(subClients => {

        const dialogRef = this.dialog.open(CompanyModalComponent, {
          width: '900px',
          data: { company, users, subClients }
        });

        dialogRef.afterClosed().subscribe(() => {
          console.log("🔄 Cerrando detalles... actualizando gráfico.");
          this.loadChartMetrics(company.id);
        });

      });
    });
  }

  logout() {
    this.authService.logout();
  }
}