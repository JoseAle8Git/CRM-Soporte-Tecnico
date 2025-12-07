import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatTableModule } from '@angular/material/table';
import { UserService } from '../../services/user-service';
import { StatsService } from '../../services/stats-service';
import { Auth } from '../../auth/auth';
import { MatDialog, MatDialogModule } from '@angular/material/dialog';
import { RoleDisplayPipe } from '../../shared/pipes/role-display-pipe';
import { UserCreateDialog } from './user-create-dialog/user-create-dialog';
import { UserBasic } from '../../models/user-basic.interface';
import { AssignmentRequest, IncidenceDashboard } from '../../models/incidence-dashboard.interface';
import { IncidenceService } from '../../services/incidence-service';
import { BaseChartDirective } from 'ng2-charts';
import { Chart, ChartConfiguration, ChartType, registerables } from 'chart.js';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatDividerModule } from '@angular/material/divider'
import { RouterLink } from '@angular/router';


@Component({
  selector: 'app-manager-dashboard',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    MatCardModule,
    MatButtonModule,
    MatTableModule,
    MatFormFieldModule,
    MatInputModule,
    MatSelectModule,
    MatIconModule,
    MatDialogModule,
    MatToolbarModule,
    MatDividerModule,
    RoleDisplayPipe,
    BaseChartDirective,
    RouterLink
  ],
  templateUrl: './manager-dashboard.html',
  styleUrl: './manager-dashboard.css',
})
export class ManagerDashboard implements OnInit {

  // Datos para la tabla de usuarios.
  userDataSource: UserBasic[] = [];
  userDisplayedColumns: string[] = ['username', 'name', 'email', 'role', 'actions'];

  // Datos para la tabla de incidencias.
  incidenceDataSource: IncidenceDashboard[] = [];
  incidenceDisplayedColumns: string[] = ['id', 'title', 'client', 'technician', 'status', 'priority', 'assign'];

  // Lista de técnicos disponibles para asignación.
  technicians: UserBasic[] = [];

  selectedTechnicians: { [key: number]: number } = {};

  constructor(
    private userService: UserService,
    private statsService: StatsService,
    private authService: Auth,
    private dialog: MatDialog,
    private incidenceService: IncidenceService
  ) { 
    Chart.register(...registerables);
  }

  ngOnInit(): void {
    // Cargar los datos iniciales al arrancar el dashboard.
    this.loadDashboardData();
  }

  loadDashboardData(): void {
    this.userService.getAllBasicUsers().subscribe({
      next: users => {
        this.userDataSource = users;
        this.technicians = users.filter(u => u.role === 'TECH');
      },
      error: err => {
        if(err.status === 401) {
          this.authService.logout();
        }
      }
    });

    this.incidenceService.findAllIncidencesForDashboard().subscribe({
      next: incidences => {
        this.incidenceDataSource = incidences;
      },
      error: err => {
        if(err.status === 401) {
          this.authService.logout();
        }
      }
    });

    this.statsService.getIncidenceCountsByTechnician().subscribe({
      next: stats => {
        this.barChartData.labels = stats.map(s => s.technicianName);
        this.barChartData.datasets[0].data = stats.map(s => s.incidenceCount);
        this.barChartData = { ...this.barChartData };
      },
      error: err => {
        if(err.status === 401) {
          this.authService.logout();
        }
      }
    });
  }

  assignIncident(incidenceId: number): void {
    const technicianId = this.selectedTechnicians[incidenceId];

    if(!technicianId) {
      alert("Por favor, selecciona un técnico.");
      return;
    }

    const request: AssignmentRequest = {
      incidenceId: incidenceId,
      technicianId: technicianId
    };

    this.incidenceService.assignTechnician(request).subscribe({
      next: (updatedIncidence) => {
        alert(`Incidencia #${updatedIncidence.id} asignada a ${updatedIncidence.technicianName}.`);
        this.loadIncidences();
      },
      error: (err) => {
        console.error("Error al asignar incidencia:", err);
        alert("Error al asignar. Verifica los logs del backend.");
      }
    });
  }

  loadIncidences(): void {
    this.incidenceService.findAllIncidencesForDashboard().subscribe(incidences => {
      this.incidenceDataSource = incidences;
    });
  }

  onLogout(): void {
    this.authService.logout();
  }

  openCreateUserDialog(): void {
    const dialogRef = this.dialog.open(UserCreateDialog, {
      width: '600px',
    });

    dialogRef.afterClosed().subscribe(result => {
      if(result === true) {
        this.loadDashboardData();
      }
    })
  }

  public barChartOptions: ChartConfiguration['options'] = {
    responsive: true,
    indexAxis: 'y',
    scales: { x: { beginAtZero: true } },
    plugins: {
      legend: { display: false }
    }
  }

  public barChartType: ChartType = 'bar';

  public barChartData: ChartConfiguration['data'] = {
    labels: [],
    datasets: [
      { data: [], label: 'Incidencias Asignadas', backgroundColor: '#3f51b5'}
    ]
  }

  /**
   * Abrir diálogo de edición. Se reutiliza el de creación.
   * @param userId
   */
  openEditUserDialog(userId: number): void {
    const dialogRef = this.dialog.open(UserCreateDialog, {
      width: '600px',
      data: { userId: userId }
    });
    dialogRef.afterClosed().subscribe(result => {
      if(result === true) {
        this.loadDashboardData();
      }
    });
  }

  confirmDelete(userId: number, userName: string): void {
    if(confirm(`¿Estás seguro de que deseas eliminar al usuario ${userName} (ID: ${userId})?`)) {
      this.userService.deleteUser(userId).subscribe({
        next: () => {
          alert(`Usuario ${userName} eliminado.`);
          this.loadDashboardData();
        },
        error: (err) => {
          console.error("Error al eliminar:", err);
          alert("Error al eliminar el usuario. Verifique sus permisos.");
        }
      })
    }
  }

}
