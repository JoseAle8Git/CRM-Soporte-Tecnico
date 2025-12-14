import { Component, Inject, inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogModule, MatDialog, MatDialogRef } from '@angular/material/dialog';
import { MatButtonModule } from '@angular/material/button';
import { MatTabsModule } from '@angular/material/tabs';
import { MatTableModule } from '@angular/material/table';
import { MatIconModule } from '@angular/material/icon';
import { CommonModule } from '@angular/common';
import { ClientData, CompanyUser, SubClient, ClientService } from '../../../../services/client';
import { AddSubClientModalComponent } from '../add-sub-client-modal/add-sub-client-modal';

export interface ModalData {
  company: ClientData;
  users: CompanyUser[];
  subClients: SubClient[];
}

@Component({
  selector: 'app-company-modal',
  standalone: true,
  imports: [
    CommonModule,
    MatDialogModule,
    MatButtonModule,
    MatTabsModule,
    MatTableModule,
    MatIconModule
  ],
  templateUrl: './company-modal.html',
  styleUrl: './company-modal.css'
})
export class CompanyModalComponent {
  constructor(@Inject(MAT_DIALOG_DATA) public data: ModalData) { }
  private dialog = inject(MatDialog);
  private dialogRef = inject(MatDialogRef<CompanyModalComponent>);
  private clientService = inject(ClientService);

  // Función para abrir el modal de creación
  openAddClient() {
    // Cerramos el modal actual momentáneamente o abrimos encima
    const dialogRef = this.dialog.open(AddSubClientModalComponent, {
      width: '400px'
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result === true) {
        // Si se creó, recargamos la lista o cerramos todo
        alert("Cliente añadido correctamente.");
        this.refreshSubClients();
      }
    });
  }
  //FUNCIÓN PARA REFRESCAR LA TABLA
  refreshSubClients() {
    const companyId = this.data.company.id;

    this.clientService.getSubClients(companyId).subscribe({
      next: (nuevosClientes) => {
        // Actualizamos la variable que usa la tabla
        this.data.subClients = nuevosClientes;

        // Mensaje de éxito discreto (opcional)
        // alert("Lista actualizada"); 
      },
      error: (e) => console.error("Error actualizando tabla", e)
    });
  }
}

