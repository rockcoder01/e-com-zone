import { Component, OnInit } from '@angular/core';
import { Store } from '@ngrx/store';
import { Observable } from 'rxjs';

import { AppState } from './store/app.state';
import * as AuthActions from './store/actions/auth.actions';
import * as CartActions from './store/actions/cart.actions';
import { AuthService } from './core/services/auth.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent implements OnInit {
  title = 'EasyBusy';
  isAuthenticated$: Observable<boolean>;

  constructor(
    private store: Store<AppState>,
    private authService: AuthService
  ) {
    this.isAuthenticated$ = this.store.select(state => state.auth.isAuthenticated);
  }

  ngOnInit(): void {
    // Check if user is already authenticated (token exists in localStorage)
    if (this.authService.isAuthenticated()) {
      // Load user profile
      this.store.dispatch(AuthActions.loadUserProfile());
      
      // Load cart items
      this.store.dispatch(CartActions.loadCart());
    }
  }
}