import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { HttpClient } from '@angular/common/http';
import { MatSnackBar } from '@angular/material/snack-bar';
import { finalize } from 'rxjs/operators';
import { environment } from '../../../../../environments/environment';

@Component({
  selector: 'app-forgot-password',
  templateUrl: './forgot-password.component.html',
  styleUrls: ['./forgot-password.component.scss']
})
export class ForgotPasswordComponent implements OnInit {
  forgotPasswordForm: FormGroup;
  loading = false;
  submitted = false;
  error = '';

  constructor(
    private fb: FormBuilder,
    private http: HttpClient,
    private snackBar: MatSnackBar
  ) {
    this.forgotPasswordForm = this.fb.group({
      email: ['', [Validators.required, Validators.email]]
    });
  }

  ngOnInit(): void {
  }

  onSubmit(): void {
    if (this.forgotPasswordForm.invalid) {
      this.forgotPasswordForm.markAllAsTouched();
      return;
    }

    this.loading = true;
    this.submitted = false;
    this.error = '';

    const { email } = this.forgotPasswordForm.value;

    this.http.post(`${environment.apiUrl}/auth/forgot-password`, { email })
      .pipe(
        finalize(() => {
          this.loading = false;
        })
      )
      .subscribe(
        () => {
          this.submitted = true;
        },
        (error) => {
          if (error.status === 404) {
            this.error = 'Email not found in our records.';
          } else {
            this.error = error.error?.message || 'An error occurred. Please try again later.';
          }
          this.snackBar.open(this.error, 'Close', {
            duration: 5000,
            panelClass: ['error-snackbar']
          });
        }
      );
  }

  getErrorMessage(controlName: string): string {
    const control = this.forgotPasswordForm.get(controlName);
    
    if (control?.hasError('required')) {
      return 'This field is required';
    }
    
    if (control?.hasError('email')) {
      return 'Please enter a valid email address';
    }
    
    return '';
  }
}