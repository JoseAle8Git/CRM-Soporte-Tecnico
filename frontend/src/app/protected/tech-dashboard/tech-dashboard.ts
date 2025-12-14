import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { TechnicianService } from '../../services/technician-service';
import { TechnicianIncidence } from '../../models/technician-incidence.interface';
import { TechnicianPersonalStats } from '../../models/tech-stats.interface';
import { BaseChartDirective } from 'ng2-charts';
import { ChartType } from 'chart.js';
import { ViewChild } from '@angular/core';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { Auth } from '../../auth/auth';

@Component({
  selector: 'app-tech-dashboard',
  standalone: true,
  imports: [CommonModule, FormsModule, BaseChartDirective, MatToolbarModule, MatButtonModule, MatIconModule],
  templateUrl: './tech-dashboard.html',
  styleUrls: ['./tech-dashboard.css']
})
export class TechDashboard implements OnInit {

  constructor(private authservice: Auth) {}

  @ViewChild(BaseChartDirective) chart?: BaseChartDirective;

  private techService = inject(TechnicianService);

  incidences: TechnicianIncidence[] = [];
  stats?: TechnicianPersonalStats;

  chartType: ChartType = 'bar';

  chartData: any = {
    labels: ['Abiertas', 'Pendientes', 'En progreso', 'Resueltas', 'Cerradas'],
    datasets: [
      {
        data: [] as number[],
        label: 'Incidencias'
      }
    ]
  };

  ngOnInit(): void {
    this.loadIncidences();
    this.loadStats();
  }

  loadIncidences() {
    this.techService.getMyIncidences().subscribe(data => {
      this.incidences = data;
    });
  }

  loadStats() {
    this.techService.getMyStats().subscribe(data => {
      this.stats = data;

      this.chartData.datasets[0].data = [
        data.open,
        data.pending,
        data.inProgress,
        data.resolved,
        data.closed
      ];

      this.chart?.update();
    });
  }

  onLogout(): void {
    this.authservice.logout();
  }

  updateStatus(incidenceId: number, newStatus: string) {
    this.techService.updateStatus(incidenceId, newStatus)
      .subscribe(() => {
        this.loadIncidences();
        this.loadStats();
      });
  }
}

