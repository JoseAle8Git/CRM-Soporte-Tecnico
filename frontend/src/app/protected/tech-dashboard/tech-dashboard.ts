import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { TechnicianService } from '../../services/technician-service';
import { TechnicianIncidence } from '../../models/technician-incidence.interface';
import { TechnicianPersonalStats } from '../../models/tech-stats.interface';
import { BaseChartDirective } from 'ng2-charts';

@Component({
  selector: 'app-tech-dashboard',
  standalone: true,
  imports: [CommonModule, FormsModule, BaseChartDirective],
  templateUrl: './tech-dashboard.html',
  styleUrls: ['./tech-dashboard.css']
})
export class TechDashboardComponent implements OnInit {

  private techService = inject(TechnicianService);

  incidences: TechnicianIncidence[] = [];
  stats?: TechnicianPersonalStats;

  chartType = 'bar';

  chartData: any = {
    labels: ['Pendientes', 'En progreso', 'Resueltas', 'Cerradas'],
    datasets: [
      {
        data: [] as number[],   // TIPO EXPLÍCITO
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

    // Rellenar gráfico
    this.chartData.datasets[0].data = [
      data.pending,
      data.inProgress,
      data.resolved,
      data.closed
    ];
  });
}


  updateStatus(incidenceId: number, newStatus: string) {
    this.techService.updateStatus(incidenceId, newStatus)
      .subscribe(() => this.loadIncidences());
  }
}
