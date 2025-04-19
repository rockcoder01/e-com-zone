import { Component, Input, OnInit } from '@angular/core';
import { Store } from '@ngrx/store';
import { Product } from '../../../../models/product.model';
import { AppState } from '../../../../store/app.state';
import * as CartActions from '../../../../store/actions/cart.actions';

@Component({
  selector: 'app-featured-products',
  templateUrl: './featured-products.component.html',
  styleUrls: ['./featured-products.component.scss']
})
export class FeaturedProductsComponent implements OnInit {
  @Input() products: Product[] | null = [];

  constructor(private store: Store<AppState>) { }

  ngOnInit(): void {
  }

  addToCart(product: Product, event: Event): void {
    event.stopPropagation();
    event.preventDefault();
    
    this.store.dispatch(CartActions.addToCart({
      productId: product.id,
      quantity: 1
    }));
  }

  calculateDiscountPercentage(original: number, current: number): number {
    if (!original || original <= current) return 0;
    return Math.round(((original - current) / original) * 100);
  }
}