import { Component, inject } from '@angular/core';
import { MatDialogRef, MatDialogModule } from '@angular/material/dialog';
import { MatButtonModule } from '@angular/material/button';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';

// imports servicios 
import { IncidenceService, CreateIncidenceDTO } from '../../../../services/incidence-service';
import { Auth } from '../../../../auth/auth';
import { MatIcon } from "@angular/material/icon";

@Component({
  selector: 'app-create-incidence-modal',
  standalone: true,
  imports: [
    CommonModule,
    MatDialogModule,
    MatButtonModule,
    MatFormFieldModule,
    MatInputModule,
    MatSelectModule,
    FormsModule,
    MatIcon
  ],
  templateUrl: './create-incidence-modal.html',
  styleUrl: './create-incidence-modal.css'
})
export class CreateIncidenceModalComponent {

  // Inyecciones
  private dialogRef = inject(MatDialogRef<CreateIncidenceModalComponent>);
  private incidenceService = inject(IncidenceService);
  private authService = inject(Auth); 

  // Objeto del formulario
  ticket: CreateIncidenceDTO = {
    title: '',
    description: '',
    priority: 'MEDIA',
    clientUserId: 0 
  };

  save() {
    // 1. Obtener ID del usuario logueado 
    const myId = this.authService.currentUserId;

    if (!myId) {
      alert('Error: No se pudo identificar tu usuario. ¿Has iniciado sesión?');
      return;
    }


    this.ticket.clientUserId = this.authService.currentUserId || 0;

    if (this.ticket.clientUserId === 0) {
      alert("Error: No encuentro tu ID de usuario en la sesión.");
      return;
    }

    // 2. Llamar al servicio para crear la incidencia
    this.incidenceService.createIncidence(this.ticket).subscribe({
      next: () => {
        alert('¡Incidencia creada con éxito! 🎫');
        this.dialogRef.close(true); // Cerramos el modal y avisamos que se creo
      },
      error: (err) => {
        console.error('Error al crear la incidencia:', err);
        alert('Error al crear la incidencia. Revisa la consola.');
      }
    });
  }
}