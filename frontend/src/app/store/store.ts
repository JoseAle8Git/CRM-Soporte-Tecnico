import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatToolbarModule } from '@angular/material/toolbar';
import { RouterLink } from '@angular/router';
import { PublicSevice } from '../services/public-sevice';

@Component({
  selector: 'app-store',
  standalone: true,
  imports: [
    CommonModule,
    RouterLink,
    ReactiveFormsModule,

    MatCardModule,
    MatButtonModule,
    MatToolbarModule,
    MatIconModule,
    MatInputModule,
    MatFormFieldModule,
    MatSelectModule
  ],
  templateUrl: './store.html',
  styleUrl: './store.css',
})
export class Store implements OnInit {

  packages = ['Plan Esencial', 'Plan Profesional', 'Plan Corporativo'];
  requestForm!: FormGroup;

  requestStatus: 'idle' | 'success' | 'error' = 'idle';

  constructor(private fb: FormBuilder, private publicService: PublicSevice) {}

  ngOnInit(): void {
    this.requestForm = this.fb.group({
      companyName: ['', Validators.required],
      cif: ['', [Validators.required, Validators.minLength(9), Validators.maxLength(20)]],
      contactName: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      packageName: ['', Validators.required],
      message: ['']
    });
  }

  selectPackage(packageName: string): void {
    this.requestForm.patchValue({ packageName: packageName });
    this.requestStatus = 'idle';
  }

  onSubmitRequest(): void {
    this.requestStatus = 'idle';

    if(this.requestForm.valid) {
      const rrquestData = this.requestForm.getRawValue();

      this.publicService.sendServiceRequest(this.requestForm.value).subscribe({
        next: () => {
          this.requestStatus = 'success';
          this.requestForm.reset();
        },
        error: (err) => {
          this.requestStatus = 'error';
          console.error("Error al enviar la solicitud: ", err);
        }
      });
    }
  }

}
