import { createReducer, on } from '@ngrx/store';
import * as AuthActions from '../actions/auth.actions';
import { User } from '../../models/user.model';

export interface AuthState {
  user: User | null;
  isAuthenticated: boolean;
  loading: boolean;
  error: string | null;
  token: string | null;
}

export const initialState: AuthState = {
  user: null,
  isAuthenticated: false,
  loading: false,
  error: null,
  token: null
};

export const authReducer = createReducer(
  initialState,
  
  // Login
  on(AuthActions.login, state => ({
    ...state,
    loading: true,
    error: null
  })),
  
  on(AuthActions.loginSuccess, (state, { user, token }) => ({
    ...state,
    user,
    token,
    isAuthenticated: true,
    loading: false,
    error: null
  })),
  
  on(AuthActions.loginFailure, (state, { error }) => ({
    ...state,
    loading: false,
    error
  })),
  
  // Register
  on(AuthActions.register, state => ({
    ...state,
    loading: true,
    error: null
  })),
  
  on(AuthActions.registerSuccess, state => ({
    ...state,
    loading: false,
    error: null
  })),
  
  on(AuthActions.registerFailure, (state, { error }) => ({
    ...state,
    loading: false,
    error
  })),
  
  // Logout
  on(AuthActions.logout, state => ({
    ...state,
    loading: true
  })),
  
  on(AuthActions.logoutSuccess, state => ({
    ...initialState
  })),
  
  // User Profile
  on(AuthActions.loadUserProfile, state => ({
    ...state,
    loading: true,
    error: null
  })),
  
  on(AuthActions.loadUserProfileSuccess, (state, { user }) => ({
    ...state,
    user,
    loading: false,
    error: null
  })),
  
  on(AuthActions.loadUserProfileFailure, (state, { error }) => ({
    ...state,
    loading: false,
    error
  })),
  
  // Update User Profile
  on(AuthActions.updateUserProfile, state => ({
    ...state,
    loading: true,
    error: null
  })),
  
  on(AuthActions.updateUserProfileSuccess, (state, { user }) => ({
    ...state,
    user,
    loading: false,
    error: null
  })),
  
  on(AuthActions.updateUserProfileFailure, (state, { error }) => ({
    ...state,
    loading: false,
    error
  })),
  
  // Change Password
  on(AuthActions.changePassword, state => ({
    ...state,
    loading: true,
    error: null
  })),
  
  on(AuthActions.changePasswordSuccess, state => ({
    ...state,
    loading: false,
    error: null
  })),
  
  on(AuthActions.changePasswordFailure, (state, { error }) => ({
    ...state,
    loading: false,
    error
  }))
);