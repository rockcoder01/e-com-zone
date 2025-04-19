import { createAction, props } from '@ngrx/store';
import { Product } from '../../models/product.model';

// Load Products Actions
export const loadProducts = createAction(
  '[Product] Load Products',
  props<{ 
    page?: number;
    limit?: number;
    categoryId?: number;
    sortBy?: string;
    sortDirection?: string;
  }>()
);

export const loadProductsSuccess = createAction(
  '[Product] Load Products Success',
  props<{ 
    products: Product[];
    totalProducts: number;
  }>()
);

export const loadProductsFailure = createAction(
  '[Product] Load Products Failure',
  props<{ error: string }>()
);

// Load Single Product Actions
export const loadProduct = createAction(
  '[Product] Load Product',
  props<{ id: number }>()
);

export const loadProductSuccess = createAction(
  '[Product] Load Product Success',
  props<{ product: Product }>()
);

export const loadProductFailure = createAction(
  '[Product] Load Product Failure',
  props<{ error: string }>()
);

// Search Products Actions
export const searchProducts = createAction(
  '[Product] Search Products',
  props<{ 
    query: string;
    page?: number;
    limit?: number;
    categoryId?: number;
    sortBy?: string;
    sortDirection?: string;
    minPrice?: number;
    maxPrice?: number;
  }>()
);

export const searchProductsSuccess = createAction(
  '[Product] Search Products Success',
  props<{ 
    products: Product[];
    totalProducts: number;
  }>()
);

export const searchProductsFailure = createAction(
  '[Product] Search Products Failure',
  props<{ error: string }>()
);

// Pagination Actions
export const setPage = createAction(
  '[Product] Set Page',
  props<{ page: number }>()
);

export const setLimit = createAction(
  '[Product] Set Limit',
  props<{ limit: number }>()
);