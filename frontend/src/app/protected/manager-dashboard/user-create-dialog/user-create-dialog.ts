import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { MatDialogModule, MatDialogRef } from '@angular/material/dialog';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { UserService } from '../../../services/user-service';
import { UserCreationRequest } from '../../../models/user-creation.interface';
import { catchError, throwError } from 'rxjs';
import { MatDivider } from '@angular/material/divider';

@Component({
  selector: 'app-user-create-dialog',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    MatDialogModule,
    MatButtonModule,
    MatFormFieldModule,
    MatInputModule,
    MatSelectModule,
    MatIconModule,
    MatDivider
  ],
  templateUrl: './user-create-dialog.html',
  styleUrl: './user-create-dialog.css',
})
export class UserCreateDialog implements OnInit {

  userForm!: FormGroup;
  creatableRoles = ['TECH', 'CLIENT'];
  isLoading = false;
  submissionError: string | null = null;

  defaultPassword = 'Pass123!@#';

  constructor(
    private fb: FormBuilder,
    private dialogRef: MatDialogRef<UserCreateDialog>,
    private userService: UserService
  ) { }

  ngOnInit(): void {
    this.userForm = this.fb.group({
      username: ['', Validators.required],
      name: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      telephone: [''],
      roleName: ['', Validators.required],
      cif: [''],
      clientId: [null],
      rawPassword: [this.defaultPassword, [Validators.required]]
    });

    this.userForm.get('roleName')?.valueChanges.subscribe(role => {
      this.toggleClientFields(role);
    });
  }

  toggleClientFields(role: string): void {
    const cifControl = this.userForm.get('cif');
    const clientIdControl = this.userForm.get('clientId');

    if(role === 'CLIENT') {
      cifControl?.setValidators(Validators.required);
      clientIdControl?.setValidators(Validators.required);
    } else {
      cifControl?.clearValidators();
      clientIdControl?.clearValidators();
    }
    cifControl?.updateValueAndValidity();
    clientIdControl?.updateValueAndValidity;
  }

  onSubmit(): void {
    this.submissionError = null;
    if(this.userForm.invalid) {
      this.userForm.markAllAsTouched();
      return;
    }

    this.isLoading = true;
    const request: UserCreationRequest = this.userForm.value;

    this.userService.createNewUser(request).pipe(
      catchError(error => {
        this.submissionError = 'Error: ' + (error.error || 'No se pudo crear el usuario.');
        return throwError(() => error);
      })
    ).subscribe({
      next: () => {
        this.dialogRef.close(true);
      },
      error: () => {
        this.isLoading = false;
      }
    })
  }

  onCancel(): void {
    this.dialogRef.close(false);
  }

}
