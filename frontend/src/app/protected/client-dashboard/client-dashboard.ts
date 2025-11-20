import { Component, inject, OnInit, signal } from '@angular/core';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatDividerModule } from '@angular/material/divider';
import { MatDialog } from '@angular/material/dialog'; // Importante para abrir la ventana

// TUS IMPORTS PROPIOS
import { Auth } from '../../auth/auth';
import { ClientService, ClientData } from '../../services/client';
import { CompanyModalComponent } from './components/company-modal/company-modal';
// ^ Asegúrate de que la ruta a 'components/company-modal...' sea correcta según tu carpeta

@Component({
  selector: 'app-client-dashboard',
  standalone: true,
  imports: [MatCardModule, MatButtonModule, MatIconModule, MatDividerModule],
  templateUrl: './client-dashboard.html',
  styleUrl: './client-dashboard.css',
})
export class ClientDashboard implements OnInit {

  // 1. Inyecciones de dependencias
  private authService = inject(Auth);
  private clientService = inject(ClientService); // Para pedir datos al backend
  private dialog = inject(MatDialog); // Para abrir el modal

  // 2. Signals
  userName = signal<string>('Cargando...');

  // Aquí guardaremos los datos de la empresa (Nombre, CIF, Plan...)
  companyData = signal<ClientData | null>(null);

  chartData = signal<any>(null);
  tickets = signal<any[]>([]);

  ngOnInit() {
    // A. Cargar nombre del usuario (Header)
    const userInDb = this.authService.getUserData();
    if (userInDb) {
      this.userName.set(userInDb.toString());
    }

    // B. Cargar datos de la empresa
    // TRUCO: Usamos el ID 1 fijo para probar lo que metimos en SQL.
    // Más adelante cambiarás el 1 por: this.authService.getClientId()
    const myClientId = 1;

    this.clientService.getClientById(myClientId).subscribe({
      next: (data) => {
        console.log('Empresa cargada:', data);
        this.companyData.set(data);
      },
      error: (e) => console.error('Error cargando empresa:', e)
    });
  }

  // 3. Función para el botón "Ver Detalles"
  // ESTA ES LA FUNCIÓN QUE LLAMA EL BOTÓN
  openDetails() {
    const company = this.companyData();

    if (!company) {
      console.error("No hay datos de empresa para mostrar");
      return;
    }

    // 1. Pedimos los usuarios de la empresa
    this.clientService.getUsersByClientId(company.id).subscribe(users => {

      // 2. Pedimos los sub-clientes (facturación)
      this.clientService.getSubClients(company.id).subscribe(subClients => {

        // 3. Abrimos el modal enviándole toda la información junta
        this.dialog.open(CompanyModalComponent, {
          width: '900px', // Un poco más ancho para que quepan las tablas
          data: {
            company: company,
            users: users,
            subClients: subClients
          }
        });

      });
    });
  }
}