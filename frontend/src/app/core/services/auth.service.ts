import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, BehaviorSubject } from 'rxjs';
import { tap } from 'rxjs/operators';
import { User } from '../../models/user.model';
import { environment } from '../../../environments/environment';

export interface LoginResponse {
  token: string;
  user: User;
}

export interface RegisterResponse {
  message: string;
}

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private currentUserSubject = new BehaviorSubject<User | null>(null);
  private tokenKey = 'auth_token';
  
  public currentUser$ = this.currentUserSubject.asObservable();

  constructor(private http: HttpClient) {
    this.initializeFromLocalStorage();
  }

  private initializeFromLocalStorage(): void {
    const token = localStorage.getItem(this.tokenKey);
    const userString = localStorage.getItem('current_user');
    
    if (token && userString) {
      try {
        const user = JSON.parse(userString);
        this.currentUserSubject.next(user);
      } catch (error) {
        this.logout();
      }
    }
  }

  getToken(): string | null {
    return localStorage.getItem(this.tokenKey);
  }

  isAuthenticated(): boolean {
    return !!this.getToken();
  }

  login(usernameOrEmail: string, password: string): Observable<LoginResponse> {
    return this.http.post<LoginResponse>(`${environment.apiUrl}/auth/login`, {
      usernameOrEmail,
      password
    }).pipe(
      tap(response => {
        localStorage.setItem(this.tokenKey, response.token);
        localStorage.setItem('current_user', JSON.stringify(response.user));
        this.currentUserSubject.next(response.user);
      })
    );
  }

  register(
    username: string,
    email: string,
    password: string,
    confirmPassword: string,
    firstName: string,
    lastName: string,
    phone?: string
  ): Observable<RegisterResponse> {
    return this.http.post<RegisterResponse>(
      `${environment.apiUrl}/auth/register`,
      {
        username,
        email,
        password,
        confirmPassword,
        firstName,
        lastName,
        phone
      }
    );
  }

  logout(): void {
    localStorage.removeItem(this.tokenKey);
    localStorage.removeItem('current_user');
    this.currentUserSubject.next(null);
  }

  getUserProfile(): Observable<User> {
    return this.http.get<User>(`${environment.apiUrl}/users/me`).pipe(
      tap(user => {
        localStorage.setItem('current_user', JSON.stringify(user));
        this.currentUserSubject.next(user);
      })
    );
  }

  updateUserProfile(
    firstName?: string,
    lastName?: string,
    phone?: string,
    profileImage?: string
  ): Observable<User> {
    return this.http.put<User>(`${environment.apiUrl}/users/me`, {
      firstName,
      lastName,
      phone,
      profileImage
    }).pipe(
      tap(user => {
        localStorage.setItem('current_user', JSON.stringify(user));
        this.currentUserSubject.next(user);
      })
    );
  }

  changePassword(
    currentPassword: string,
    newPassword: string,
    confirmPassword: string
  ): Observable<any> {
    return this.http.post(`${environment.apiUrl}/users/change-password`, {
      currentPassword,
      newPassword,
      confirmPassword
    });
  }
}