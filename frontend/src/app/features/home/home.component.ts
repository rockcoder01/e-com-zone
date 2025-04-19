import { Component, OnInit } from '@angular/core';
import { Store } from '@ngrx/store';
import { Observable } from 'rxjs';

import { AppState } from '../../store/app.state';
import { Product } from '../../models/product.model';
import * as ProductActions from '../../store/actions/product.actions';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss']
})
export class HomeComponent implements OnInit {
  featuredProducts$: Observable<Product[]>;
  loading$: Observable<boolean>;

  constructor(private store: Store<AppState>) {
    this.featuredProducts$ = this.store.select(state => state.products.products);
    this.loading$ = this.store.select(state => state.products.loading);
  }

  ngOnInit(): void {
    // Load featured products (newest releases)
    this.store.dispatch(ProductActions.loadProducts({
      page: 1,
      limit: 8,
      sortBy: 'createdAt',
      sortDirection: 'desc'
    }));
  }
}