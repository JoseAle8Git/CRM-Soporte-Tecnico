import { Component, inject } from '@angular/core';
import { MatDialogRef, MatDialogModule } from '@angular/material/dialog';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatCheckboxModule } from '@angular/material/checkbox';
import { FormsModule } from '@angular/forms';
import { ClientService } from '../../../../services/client';

@Component({
  selector: 'app-add-sub-client-modal',
  standalone: true,
  imports: [MatDialogModule, MatFormFieldModule, MatInputModule, MatButtonModule, FormsModule, MatCheckboxModule],
  templateUrl: './add-sub-client-modal.html',
  styles: [`
    .form-container { display: flex; flex-direction: column; gap: 15px; min-width: 300px; }
  `]
})
export class AddSubClientModalComponent {
  private dialogRef = inject(MatDialogRef<AddSubClientModalComponent>);
  private clientService = inject(ClientService);

  // Datos del formulario
  data = {
    name: '',
    billing: 0,
    active: true
  };

  save() {
    if(!this.data.name) return;

    this.clientService.createSubClient(this.data).subscribe({
      next: () => this.dialogRef.close(true), // Devolvemos true si sale bien
      error: (e) => console.error(e)
    });
  }

  close() {
    this.dialogRef.close(false);
  }
}