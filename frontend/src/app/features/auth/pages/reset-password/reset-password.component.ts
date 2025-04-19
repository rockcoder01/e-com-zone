import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators, AbstractControl, ValidationErrors } from '@angular/forms';
import { HttpClient } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';
import { MatSnackBar } from '@angular/material/snack-bar';
import { finalize } from 'rxjs/operators';
import { environment } from '../../../../../environments/environment';

@Component({
  selector: 'app-reset-password',
  templateUrl: './reset-password.component.html',
  styleUrls: ['./reset-password.component.scss']
})
export class ResetPasswordComponent implements OnInit {
  resetPasswordForm: FormGroup;
  loading = false;
  submitted = false;
  error = '';
  token = '';
  hidePassword = true;
  hideConfirmPassword = true;

  constructor(
    private fb: FormBuilder,
    private http: HttpClient,
    private route: ActivatedRoute,
    private router: Router,
    private snackBar: MatSnackBar
  ) {
    this.resetPasswordForm = this.fb.group({
      password: ['', [
        Validators.required, 
        Validators.minLength(8),
        Validators.pattern(/^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)[a-zA-Z\d@$!%*?&]{8,}$/)
      ]],
      confirmPassword: ['', [Validators.required]]
    }, {
      validators: this.passwordMatchValidator
    });
  }

  ngOnInit(): void {
    // Get token from URL
    this.route.queryParams.subscribe(params => {
      if (params['token']) {
        this.token = params['token'];
      } else {
        this.error = 'Invalid or missing reset token. Please request a new password reset link.';
      }
    });
  }

  // Custom validator to check if passwords match
  passwordMatchValidator(control: AbstractControl): ValidationErrors | null {
    const password = control.get('password');
    const confirmPassword = control.get('confirmPassword');
    
    if (password && confirmPassword && password.value !== confirmPassword.value) {
      confirmPassword.setErrors({ passwordMismatch: true });
      return { passwordMismatch: true };
    }
    
    return null;
  }

  onSubmit(): void {
    if (this.resetPasswordForm.invalid || !this.token) {
      this.resetPasswordForm.markAllAsTouched();
      return;
    }

    this.loading = true;
    this.error = '';

    const { password } = this.resetPasswordForm.value;

    this.http.post(`${environment.apiUrl}/auth/reset-password`, { 
      token: this.token, 
      password 
    })
    .pipe(
      finalize(() => {
        this.loading = false;
      })
    )
    .subscribe(
      () => {
        this.submitted = true;
        
        // Auto-redirect to login page after 3 seconds
        setTimeout(() => {
          this.router.navigate(['/auth/login']);
        }, 3000);
      },
      (error) => {
        if (error.status === 400) {
          this.error = 'The reset token is invalid or has expired.';
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
    const control = this.resetPasswordForm.get(controlName);
    
    if (control?.hasError('required')) {
      return 'This field is required';
    }
    
    if (control?.hasError('minlength')) {
      return `Must be at least ${control.getError('minlength').requiredLength} characters`;
    }
    
    if (control?.hasError('pattern')) {
      return 'Password must contain at least one uppercase letter, one lowercase letter, and one number';
    }
    
    if (control?.hasError('passwordMismatch')) {
      return 'Passwords do not match';
    }
    
    return '';
  }
}