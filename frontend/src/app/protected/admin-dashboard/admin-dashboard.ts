import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { MatDividerModule } from '@angular/material/divider';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatOption, MatSelectModule } from '@angular/material/select';
import { MatTableModule } from '@angular/material/table';
import { MatToolbarModule } from '@angular/material/toolbar';
import { BaseChartDirective } from 'ng2-charts';
import { Chart, ChartConfiguration, ChartData, ChartType, registerables } from 'chart.js';
import { UserService } from '../../services/user-service';
import { StatsService } from '../../services/stats-service';
import { Auth } from '../../auth/auth';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { MatIconModule } from '@angular/material/icon';
import { RouterLink } from '@angular/router';
import { UserBasic } from '../../models/user-basic.interface';
import { ClientInfo, PackageCount } from '../../models/client-dashboard.interface';
import { UserCreateDialog } from '../manager-dashboard/user-create-dialog/user-create-dialog';
import { MatDialog } from '@angular/material/dialog';
import { MatProgressSpinner, MatProgressSpinnerModule, ProgressSpinnerMode } from '@angular/material/progress-spinner';
import { ResportService } from '../../services/resport.service';
import { ReportLog } from '../../models/report-log.interface';

@Component({
  selector: 'app-admin-dashboard',
  standalone: true,
  imports: [
    CommonModule,
    MatCardModule,
    MatTableModule,
    MatSelectModule,
    MatFormFieldModule,
    MatInputModule,
    MatDividerModule,
    MatButtonModule,
    MatToolbarModule,
    BaseChartDirective,
    FormsModule,
    MatIconModule,
    ReactiveFormsModule,
    RouterLink,
    MatProgressSpinnerModule
  ],
  templateUrl: './admin-dashboard.html',
  styleUrl: './admin-dashboard.css',
})
export class AdminDashboard implements OnInit {

  userDataSource: UserBasic[] = [];
  userDisplayedColumns: string[] = ['username', 'name', 'email', 'role', 'actions'];

  clientDataSource: ClientInfo[] = [];
  clientDisplayedColumns: string[] = ['companyName', 'cif', 'servicePackage', 'active', 'direction'];

  reportLogDataSource: ReportLog[] = [];
  reportLogDisplayedColumns: string[] = ['date', 'type', 'emailSent', 'actions'];

  allClients: ClientInfo[] = [];

  activeFilter: boolean | null = null;
  packageFilter: string | null = null;
  packageOptions = ['Plan Esencial', 'Plan Profesional', 'Plan Corporativo'];

  public pieChartOptions: ChartConfiguration['options'] = {
    responsive: true,
    plugins: { legend: { position: 'right' as const, labels: { boxWidth: 10 }}}
  };
  public pieChartType: ChartType = 'pie';
  public pieChartData: ChartData<'pie', number[], string | string[]> = {
    labels: [],
    datasets: [{ data: [], label: 'Paquetes Activos', backgroundColor: ['#3f51b5', '#ff4081', '#ffc107'] }]
  };

  proyectedRevenue: number = 0;
  isLoading: boolean = true;

  constructor(
    private userService: UserService,
    private statsService: StatsService,
    private authservice: Auth,
    private dialog: MatDialog,
    private reportService: ResportService
  ) {
    Chart.register(...registerables)
  }

  ngOnInit(): void {
    this.loadAdminData();
  }

  loadAdminData(): void {
    this.isLoading = true;
    this.userService.getAllBasicUsers().subscribe({
      next: users => {
        this.userDataSource = this.sortUsersByRole(users);
        this.checkLoadingComplete();
      },
      error: err => {
        this.handleApiError(err);
      }
    })

    this.userService.getAllClients().subscribe({
      next: (clients: ClientInfo[]) => {
        console.log("Clientes cargados:", clients);

        this.allClients = clients;
        this.clientDataSource = clients;

        this.isLoading = false;
      },
      error: () => {
        this.isLoading = false;
      }
    });

    this.statsService.getProjectedMonthlyRevenue().subscribe({
      next: revenue => {
        this.proyectedRevenue = revenue;
        this.checkLoadingComplete();
      },
      error: err => {
        this.handleApiError(err);
      }
    });

    this.statsService.getActiveCountsByPackage().subscribe({
      next: data => {
        this.updatePieChart(data);
        this.checkLoadingComplete();
      },
      error: err => {
        this.handleApiError(err);
      }
    });

    this.reportService.getReportLogs().subscribe({
      next: logs => {
        this.reportLogDataSource = logs;
        this.checkLoadingComplete();
      },
      error: err => {
        this.handleApiError(err);
      }
    })

    this.expectedLoads = 5;
  }

  sortUsersByRole(users: UserBasic[]): UserBasic[] {
    const order = { 'ADMIN': 1, 'MANAGER': 2, 'TECNICO': 3, 'CLIENTE': 4 };
    return users.sort((a, b) => {
      const roleOrder = order as any;
        const orderA = roleOrder[a.role] || 99;
        const orderB = roleOrder[b.role] || 99;
        return orderA - orderB;
    });
  }

  applyClientFilters(): void {
    this.clientDataSource = this.allClients.filter(client => {
      const matchesPackage = this.packageFilter === null || client.servicePackage === this.packageFilter;
      const matchesActive = this.activeFilter === null || client.active === this.activeFilter;

      return matchesPackage && matchesActive;
    });
  }

  updatePieChart(data: PackageCount[]): void {
    this.pieChartData = {
      labels: data.map(d => d.packageName),
      datasets: [{
        data: data.map(d => d.clientCount),
        backgroundColor: ['#3f51b5', '#ff4081', '#ffc107']
      }]
    };
    this.pieChartData = {...this.pieChartData};
  }

  onLogout(): void {
    this.authservice.logout();
  }


  openCreateUserDialog(): void {
    const dialogRef = this.dialog.open(UserCreateDialog, {
      width: '600px',
    });
  
    dialogRef.afterClosed().subscribe(result => {
      if(result === true) {
        this.loadAdminData();
      }
    })
  }

  openEditUserDialog(userId: number): void {
    const dialogRef = this.dialog.open(UserCreateDialog, {
      width: '600px',
      data: { userId: userId }
    });
    dialogRef.afterClosed().subscribe(result => {
      if(result === true) {
        this.loadAdminData();
      }
    });
  }

  confirmDelete(userId: number, userName: string): void {
    if(confirm(`¿Estás seguro de que deseas eliminar al usuario ${userName} (ID: ${userId})?`)) {
      this.userService.deleteUser(userId).subscribe({
        next: () => {
          alert(`Usuario ${userName} eliminado.`);
          this.loadAdminData();
        },
        error: (err) => {
          console.error("Error al eliminar:", err);
          alert("Error al eliminar el usuario. Verifique sus permisos.");
        }
      })
    }
  }

  handleApiError(err: any): void {
    if(err.status === 401 || err.status === 403) {
      this.authservice.logout();
    } else {
      console.error("Error al cargar datos de Dashboard:", err);
      this.isLoading = false;
    }
  }

  private loadedCount = 0;
  private expectedLoads = 4;

  checkLoadingComplete(): void {
    this.loadedCount++;
    if(this.loadedCount === this.expectedLoads) {
      this.isLoading = false;
      this.loadedCount = 0;
    }
  }

  openPdf(reportId: number): void {
    const url = this.reportService.getDownloadUrl(reportId);
    window.open(url, '_blank');
  }

}
