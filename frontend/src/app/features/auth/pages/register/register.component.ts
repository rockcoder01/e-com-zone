import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators, AbstractControl, ValidationErrors } from '@angular/forms';
import { Store } from '@ngrx/store';
import { Observable } from 'rxjs';

import { AppState } from '../../../../store/app.state';
import * as AuthActions from '../../../../store/actions/auth.actions';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.scss']
})
export class RegisterComponent implements OnInit {
  registerForm: FormGroup;
  loading$: Observable<boolean>;
  error$: Observable<string | null>;
  hidePassword = true;
  hideConfirmPassword = true;

  constructor(
    private fb: FormBuilder,
    private store: Store<AppState>
  ) {
    this.registerForm = this.fb.group({
      firstName: ['', [Validators.required, Validators.maxLength(50)]],
      lastName: ['', [Validators.required, Validators.maxLength(50)]],
      email: ['', [Validators.required, Validators.email]],
      phone: ['', [Validators.pattern(/^\+?[0-9\s-]{10,15}$/)]],
      password: ['', [
        Validators.required, 
        Validators.minLength(8),
        Validators.pattern(/^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)[a-zA-Z\d@$!%*?&]{8,}$/)
      ]],
      confirmPassword: ['', [Validators.required]],
      terms: [false, [Validators.requiredTrue]]
    }, {
      validators: this.passwordMatchValidator
    });

    this.loading$ = this.store.select(state => state.auth.loading);
    this.error$ = this.store.select(state => state.auth.error);
  }

  ngOnInit(): void {
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
    if (this.registerForm.valid) {
      const { firstName, lastName, email, phone, password } = this.registerForm.value;
      
      this.store.dispatch(AuthActions.register({
        firstName,
        lastName,
        email,
        phone,
        password
      }));
    } else {
      this.registerForm.markAllAsTouched();
    }
  }

  getErrorMessage(controlName: string): string {
    const control = this.registerForm.get(controlName);
    
    if (control?.hasError('required')) {
      return 'This field is required';
    }
    
    if (control?.hasError('email')) {
      return 'Please enter a valid email address';
    }
    
    if (control?.hasError('minlength')) {
      return `Must be at least ${control.getError('minlength').requiredLength} characters`;
    }
    
    if (control?.hasError('maxlength')) {
      return `Cannot exceed ${control.getError('maxlength').requiredLength} characters`;
    }
    
    if (control?.hasError('pattern')) {
      if (controlName === 'password') {
        return 'Password must contain at least one uppercase letter, one lowercase letter, and one number';
      }
      if (controlName === 'phone') {
        return 'Please enter a valid phone number';
      }
      return 'Invalid format';
    }
    
    if (control?.hasError('passwordMismatch')) {
      return 'Passwords do not match';
    }
    
    return '';
  }
}