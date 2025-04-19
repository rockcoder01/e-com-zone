import { Injectable } from '@angular/core';
import { Actions, createEffect, ofType } from '@ngrx/effects';
import { of } from 'rxjs';
import { catchError, map, mergeMap } from 'rxjs/operators';

import * as ProductActions from '../actions/product.actions';
import { ProductService } from '../../core/services/product.service';

@Injectable()
export class ProductEffects {
  loadProducts$ = createEffect(() =>
    this.actions$.pipe(
      ofType(ProductActions.loadProducts),
      mergeMap((action) =>
        this.productService.getProducts(
          action.page,
          action.limit,
          action.categoryId,
          action.sortBy,
          action.sortDirection
        ).pipe(
          map(response => ProductActions.loadProductsSuccess({
            products: response.products,
            totalProducts: response.totalCount
          })),
          catchError(error => of(ProductActions.loadProductsFailure({
            error: error.error?.message || 'Failed to load products.'
          })))
        )
      )
    )
  );

  loadProduct$ = createEffect(() =>
    this.actions$.pipe(
      ofType(ProductActions.loadProduct),
      mergeMap(({ id }) =>
        this.productService.getProductById(id).pipe(
          map(product => ProductActions.loadProductSuccess({ product })),
          catchError(error => of(ProductActions.loadProductFailure({
            error: error.error?.message || 'Failed to load product details.'
          })))
        )
      )
    )
  );

  searchProducts$ = createEffect(() =>
    this.actions$.pipe(
      ofType(ProductActions.searchProducts),
      mergeMap((action) =>
        this.productService.searchProducts(
          action.query,
          action.page,
          action.limit,
          action.categoryId,
          action.sortBy,
          action.sortDirection,
          action.minPrice,
          action.maxPrice
        ).pipe(
          map(response => ProductActions.searchProductsSuccess({
            products: response.products,
            totalProducts: response.totalCount
          })),
          catchError(error => of(ProductActions.searchProductsFailure({
            error: error.error?.message || 'Failed to search products.'
          })))
        )
      )
    )
  );

  constructor(
    private actions$: Actions,
    private productService: ProductService
  ) {}
}