import { CommonModule } from '@angular/common';
import { Component, Inject, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { MAT_DIALOG_DATA, MatDialogModule, MatDialogRef } from '@angular/material/dialog';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { UserService } from '../../../services/user-service';
import { UserCreationRequest } from '../../../models/user-creation.interface';
import { catchError, Observable, throwError } from 'rxjs';
import { MatDivider } from '@angular/material/divider';
import { UserBasic } from '../../../models/user-basic.interface';

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
  servicePackages = ['Plan Esencial', 'Plan Profesional', 'Plan Corporativo']
  isLoading = false;
  submissionError: string | null = null;

  defaultPassword = 'Pass123!@#';

  isEditMode = false;

  constructor(
    private fb: FormBuilder,
    private dialogRef: MatDialogRef<UserCreateDialog>,
    private userService: UserService,
    @Inject(MAT_DIALOG_DATA) public data: { userId: number } | null
  ) { }

  ngOnInit(): void {
    this.userForm = this.fb.group({
      username: ['', Validators.required],
      name: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      telephone: [''],
      roleName: ['', Validators.required],
      companyName: [''],
      packageName: [''],
      cif: ['', Validators.required],
      rawPassword: [this.defaultPassword, [Validators.required]]
    });

    if(this.data && this.data.userId) {
      this.isEditMode = true;
      this.userService.getUserById(this.data.userId).subscribe(user => {
        this.userForm.patchValue(user);
        this.userForm.get('username')?.disable();
      })
    }

    this.userForm.get('roleName')?.valueChanges.subscribe(role => {
      this.toggleClientFields(role);
    });
  }

  toggleClientFields(role: string): void {
    const cifControl = this.userForm.get('cif');
    const companyControl = this.userForm.get('companyName');
    const packageControl = this.userForm.get('packageName');

    if(role === 'CLIENT') {
      cifControl?.setValidators(Validators.required);
      companyControl?.setValidators(Validators.required);
      packageControl?.setValidators(Validators.required);
    } else {
      cifControl?.clearValidators();
      companyControl?.clearValidators();
      packageControl?.clearValidators();

      cifControl?.setValue(null);
      companyControl?.setValue('');
      packageControl?.setValue('');
    }
    cifControl?.updateValueAndValidity();
    companyControl?.updateValueAndValidity();
    packageControl?.updateValueAndValidity();
  }

  onSubmit(): void {
    this.submissionError = null;
    if(this.userForm.invalid) {
      this.userForm.markAllAsTouched();
      return;
    }

    this.isLoading = true;
    const request: UserCreationRequest = this.userForm.getRawValue() as UserCreationRequest;

    let operations: Observable<UserBasic>;

    if(this.isEditMode) {
      operations = this.userService.updateUser(this.data!.userId, request);
    } else {
      operations = this.userService.createNewUser(request);
    }

    operations.pipe(
      catchError(error => {
        this.submissionError = 'Error' + (error.error || 'No se pudo guardar el servicio.');
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
