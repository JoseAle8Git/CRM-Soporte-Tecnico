import { Component, inject } from '@angular/core';
import { MatDialogRef, MatDialogModule } from '@angular/material/dialog';
import { MatButtonModule } from '@angular/material/button';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';

// IMPORTS DE TUS SERVICIOS (Subimos 4 niveles para llegar a 'app')
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
  templateUrl: './create-incidence-modal.html', // Asegúrate de que el nombre coincide con tu archivo
  styleUrl: './create-incidence-modal.css'
})
export class CreateIncidenceModalComponent {

  // Inyecciones
  private dialogRef = inject(MatDialogRef<CreateIncidenceModalComponent>);
  private incidenceService = inject(IncidenceService); // El servicio de tu compi
  private authService = inject(Auth); // Tu servicio de Auth

  // Objeto del formulario
  ticket: CreateIncidenceDTO = {
    title: '',
    description: '',
    priority: 'MEDIA',
    clientUserId: 0 // Lo rellenamos al guardar
  };

  save() {
    // 1. Obtener ID del usuario logueado (Juanito)
    // OJO: Revisa si en tu Auth el campo se llama 'userId', 'userid' o 'id'
    const myId = this.authService.currentUserId;
    // O si usas la propiedad pública: const myUser = this.authService.currentUser;

    if (!myId) {
      alert('Error: No se pudo identificar tu usuario. ¿Has iniciado sesión?');
      return;
    }

    // Asignamos el ID (asumiendo que getUserData devuelve un objeto con id o userId)
    // Si getUserData() devuelve solo el nombre, tendrás que usar this.authService.currentUser.userId
    // Usamos el getter público (sin el signo de interrogación porque el getter ya maneja el null)
    this.ticket.clientUserId = this.authService.currentUserId || 0;

    if (this.ticket.clientUserId === 0) {
      alert("Error: No encuentro tu ID de usuario en la sesión.");
      return;
    }

    // 2. Llamar al servicio para crear la incidencia
    this.incidenceService.createIncidence(this.ticket).subscribe({
      next: () => {
        alert('¡Incidencia creada con éxito! 🎫');
        this.dialogRef.close(true); // Cerramos el modal y avisamos que se creó
      },
      error: (err) => {
        console.error('Error al crear la incidencia:', err);
        alert('Error al crear la incidencia. Revisa la consola.');
      }
    });
    // --- AÑADE ESTOS LOGS AQUÍ ---
    console.log('=== 🔍 INSPECCIONANDO EL USUARIO ===');


    // 2. Vemos qué intenta sacar el getter (si lo tienes)
    console.log('Getter currentUserId:', this.authService.currentUserId);

    // 3. Vemos qué ID has metido en el ticket
    console.log('ID asignado al ticket:', this.ticket.clientUserId);
    console.log('====================================');

    // ... aquí sigue tu código de validación (if !myId ...) y el envío ...
  }
}