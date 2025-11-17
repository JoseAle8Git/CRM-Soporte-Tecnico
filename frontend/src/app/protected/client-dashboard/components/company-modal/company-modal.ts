import { Component, Inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogModule } from '@angular/material/dialog';
import { MatButtonModule } from '@angular/material/button';
import { MatTabsModule } from '@angular/material/tabs';
import { MatTableModule } from '@angular/material/table';
import { MatIconModule } from '@angular/material/icon';
import { CommonModule } from '@angular/common';
import { ClientData, CompanyUser, SubClient } from '../../../../services/client';

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
  constructor(@Inject(MAT_DIALOG_DATA) public data: ModalData) {}
}