import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Store } from '@ngrx/store';
import { Observable } from 'rxjs';
import { AppState } from '../../../store/app.state';
import { selectCurrentUser, selectIsAuthenticated } from '../../../store/reducers/auth.reducer';
import { User } from '../../../models/user.model';
import { logout } from '../../../store/actions/auth.actions';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.scss']
})
export class HeaderComponent implements OnInit {
  isAuthenticated$: Observable<boolean>;
  currentUser$: Observable<User | null>;
  
  constructor(
    private store: Store<AppState>,
    private router: Router
  ) {
    this.isAuthenticated$ = this.store.select(selectIsAuthenticated);
    this.currentUser$ = this.store.select(selectCurrentUser);
  }

  ngOnInit(): void {}

  onLogout(): void {
    this.store.dispatch(logout());
  }

  navigateToProfile(): void {
    this.router.navigate(['/user/profile']);
  }

  navigateToCart(): void {
    this.router.navigate(['/cart']);
  }

  navigateToWishlist(): void {
    this.router.navigate(['/user/wishlist']);
  }
}