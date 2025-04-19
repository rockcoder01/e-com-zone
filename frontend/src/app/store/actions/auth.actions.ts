import { createAction, props } from '@ngrx/store';
import { User } from '../../models/user.model';

// Login Actions
export const login = createAction(
  '[Auth] Login',
  props<{ usernameOrEmail: string; password: string }>()
);

export const loginSuccess = createAction(
  '[Auth] Login Success',
  props<{ user: User; token: string }>()
);

export const loginFailure = createAction(
  '[Auth] Login Failure',
  props<{ error: string }>()
);

// Register Actions
export const register = createAction(
  '[Auth] Register',
  props<{
    username: string;
    email: string;
    password: string;
    confirmPassword: string;
    firstName: string;
    lastName: string;
    phone?: string;
  }>()
);

export const registerSuccess = createAction(
  '[Auth] Register Success',
  props<{ message: string }>()
);

export const registerFailure = createAction(
  '[Auth] Register Failure',
  props<{ error: string }>()
);

// Logout Actions
export const logout = createAction('[Auth] Logout');

export const logoutSuccess = createAction('[Auth] Logout Success');

// User Profile Actions
export const loadUserProfile = createAction('[Auth] Load User Profile');

export const loadUserProfileSuccess = createAction(
  '[Auth] Load User Profile Success',
  props<{ user: User }>()
);

export const loadUserProfileFailure = createAction(
  '[Auth] Load User Profile Failure',
  props<{ error: string }>()
);

// Update User Profile Actions
export const updateUserProfile = createAction(
  '[Auth] Update User Profile',
  props<{
    firstName?: string;
    lastName?: string;
    phone?: string;
    profileImage?: string;
  }>()
);

export const updateUserProfileSuccess = createAction(
  '[Auth] Update User Profile Success',
  props<{ user: User }>()
);

export const updateUserProfileFailure = createAction(
  '[Auth] Update User Profile Failure',
  props<{ error: string }>()
);

// Change Password Actions
export const changePassword = createAction(
  '[Auth] Change Password',
  props<{
    currentPassword: string;
    newPassword: string;
    confirmPassword: string;
  }>()
);

export const changePasswordSuccess = createAction(
  '[Auth] Change Password Success',
  props<{ message: string }>()
);

export const changePasswordFailure = createAction(
  '[Auth] Change Password Failure',
  props<{ error: string }>()
);