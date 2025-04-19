import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { Actions, createEffect, ofType } from '@ngrx/effects';
import { of } from 'rxjs';
import { catchError, map, exhaustMap, tap } from 'rxjs/operators';

import * as AuthActions from '../actions/auth.actions';
import { AuthService } from '../../core/services/auth.service';

@Injectable()
export class AuthEffects {
  login$ = createEffect(() =>
    this.actions$.pipe(
      ofType(AuthActions.login),
      exhaustMap(action =>
        this.authService.login(action.usernameOrEmail, action.password).pipe(
          map(response => AuthActions.loginSuccess({
            user: response.user,
            token: response.token
          })),
          catchError(error => of(AuthActions.loginFailure({
            error: error.error?.message || 'Invalid credentials. Please try again.'
          })))
        )
      )
    )
  );

  loginSuccess$ = createEffect(() =>
    this.actions$.pipe(
      ofType(AuthActions.loginSuccess),
      tap(() => this.router.navigate(['/']))
    ),
    { dispatch: false }
  );

  register$ = createEffect(() =>
    this.actions$.pipe(
      ofType(AuthActions.register),
      exhaustMap(action =>
        this.authService.register(
          action.username,
          action.email,
          action.password,
          action.confirmPassword,
          action.firstName,
          action.lastName,
          action.phone
        ).pipe(
          map((response: any) => AuthActions.registerSuccess({ message: response.message })),
          catchError(error => of(AuthActions.registerFailure({
            error: error.error?.message || 'Registration failed. Please try again.'
          })))
        )
      )
    )
  );

  registerSuccess$ = createEffect(() =>
    this.actions$.pipe(
      ofType(AuthActions.registerSuccess),
      tap(() => this.router.navigate(['/auth/login']))
    ),
    { dispatch: false }
  );

  logout$ = createEffect(() =>
    this.actions$.pipe(
      ofType(AuthActions.logout),
      tap(() => {
        this.authService.logout();
        this.router.navigate(['/auth/login']);
      }),
      map(() => AuthActions.logoutSuccess())
    )
  );

  loadUserProfile$ = createEffect(() =>
    this.actions$.pipe(
      ofType(AuthActions.loadUserProfile),
      exhaustMap(() =>
        this.authService.getUserProfile().pipe(
          map(user => AuthActions.loadUserProfileSuccess({ user })),
          catchError(error => of(AuthActions.loadUserProfileFailure({
            error: error.error?.message || 'Failed to load user profile.'
          })))
        )
      )
    )
  );

  updateUserProfile$ = createEffect(() =>
    this.actions$.pipe(
      ofType(AuthActions.updateUserProfile),
      exhaustMap(action =>
        this.authService.updateUserProfile(
          action.firstName,
          action.lastName,
          action.phone,
          action.profileImage
        ).pipe(
          map(user => AuthActions.updateUserProfileSuccess({ user })),
          catchError(error => of(AuthActions.updateUserProfileFailure({
            error: error.error?.message || 'Failed to update user profile.'
          })))
        )
      )
    )
  );

  changePassword$ = createEffect(() =>
    this.actions$.pipe(
      ofType(AuthActions.changePassword),
      exhaustMap(action =>
        this.authService.changePassword(
          action.currentPassword,
          action.newPassword,
          action.confirmPassword
        ).pipe(
          map((response: any) => AuthActions.changePasswordSuccess({
            message: 'Password changed successfully.'
          })),
          catchError(error => of(AuthActions.changePasswordFailure({
            error: error.error?.message || 'Failed to change password.'
          })))
        )
      )
    )
  );

  constructor(
    private actions$: Actions,
    private authService: AuthService,
    private router: Router
  ) {}
}