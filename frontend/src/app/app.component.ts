import { Component, OnInit } from '@angular/core';
import { Store } from '@ngrx/store';
import { Observable } from 'rxjs';
import { AppState } from './store/app.state';
import { getCurrentUser } from './store/actions/auth.actions';
import { selectIsAuthenticated } from './store/reducers/auth.reducer';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent implements OnInit {
  title = 'EasyBusy';
  isAuthenticated$: Observable<boolean>;

  constructor(
    private store: Store<AppState>
  ) {
    this.isAuthenticated$ = this.store.select(selectIsAuthenticated);
  }

  ngOnInit(): void {
    // Try to auto-login if there's an existing token
    this.store.dispatch(getCurrentUser());
  }
}