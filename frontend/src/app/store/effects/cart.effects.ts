import { Injectable } from '@angular/core';
import { Actions, createEffect, ofType } from '@ngrx/effects';
import { of } from 'rxjs';
import { catchError, map, mergeMap } from 'rxjs/operators';

import * as CartActions from '../actions/cart.actions';
import { CartService } from '../../core/services/cart.service';

@Injectable()
export class CartEffects {
  loadCart$ = createEffect(() =>
    this.actions$.pipe(
      ofType(CartActions.loadCart),
      mergeMap(() =>
        this.cartService.getCart().pipe(
          map(items => CartActions.loadCartSuccess({ items })),
          catchError(error => of(CartActions.loadCartFailure({
            error: error.error?.message || 'Failed to load cart items.'
          })))
        )
      )
    )
  );

  addToCart$ = createEffect(() =>
    this.actions$.pipe(
      ofType(CartActions.addToCart),
      mergeMap((action) => {
        const item = {
          productId: action.productId,
          name: action.name,
          price: action.price,
          quantity: action.quantity,
          image: action.image,
          sku: action.sku
        };
        
        return this.cartService.addToCart(
          action.productId,
          action.quantity
        ).pipe(
          map(() => CartActions.addToCartSuccess({ item })),
          catchError(error => of(CartActions.addToCartFailure({
            error: error.error?.message || 'Failed to add item to cart.'
          })))
        );
      })
    )
  );

  updateCartItem$ = createEffect(() =>
    this.actions$.pipe(
      ofType(CartActions.updateCartItem),
      mergeMap(({ productId, quantity }) =>
        this.cartService.updateCartItem(productId, quantity).pipe(
          map(() => CartActions.updateCartItemSuccess({ productId, quantity })),
          catchError(error => of(CartActions.updateCartItemFailure({
            error: error.error?.message || 'Failed to update cart item.'
          })))
        )
      )
    )
  );

  removeFromCart$ = createEffect(() =>
    this.actions$.pipe(
      ofType(CartActions.removeFromCart),
      mergeMap(({ productId }) =>
        this.cartService.removeFromCart(productId).pipe(
          map(() => CartActions.removeFromCartSuccess({ productId })),
          catchError(error => of(CartActions.removeFromCartFailure({
            error: error.error?.message || 'Failed to remove item from cart.'
          })))
        )
      )
    )
  );

  clearCart$ = createEffect(() =>
    this.actions$.pipe(
      ofType(CartActions.clearCart),
      mergeMap(() =>
        this.cartService.clearCart().pipe(
          map(() => CartActions.clearCartSuccess()),
          catchError(error => of(CartActions.clearCartFailure({
            error: error.error?.message || 'Failed to clear cart.'
          })))
        )
      )
    )
  );

  constructor(
    private actions$: Actions,
    private cartService: CartService
  ) {}
}