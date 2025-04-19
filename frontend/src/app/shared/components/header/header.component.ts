import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { Store } from '@ngrx/store';

import { User } from '../../../models/user.model';
import { AuthService } from '../../../core/services/auth.service';
import { AppState } from '../../../store/app.state';
import * as AuthActions from '../../../store/actions/auth.actions';
import * as fromAuth from '../../../store/reducers/auth.reducer';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.scss']
})
export class HeaderComponent implements OnInit {
  isAuthenticated$: Observable<boolean>;
  currentUser$: Observable<User | null>;
  cartItemCount$: Observable<number>;

  constructor(
    private store: Store<AppState>,
    private authService: AuthService,
    private router: Router
  ) {
    this.isAuthenticated$ = this.store.select(
      state => state.auth.isAuthenticated
    );
    
    this.currentUser$ = this.store.select(
      state => state.auth.user
    );
    
    this.cartItemCount$ = this.store.select(
      state => state.cart.totalItems
    );
  }

  ngOnInit(): void {}

  onLogout(): void {
    this.store.dispatch(AuthActions.logout());
  }

  navigateToProfile(): void {
    this.router.navigate(['/account/profile']);
  }

  navigateToCart(): void {
    this.router.navigate(['/cart']);
  }

  navigateToWishlist(): void {
    this.router.navigate(['/account/wishlist']);
  }
}