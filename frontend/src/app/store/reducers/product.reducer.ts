import { createReducer, on } from '@ngrx/store';
import { Product } from '../../models/product.model';
import * as ProductActions from '../actions/product.actions';

export interface ProductState {
  products: Product[];
  selectedProduct: Product | null;
  loading: boolean;
  error: string | null;
  totalProducts: number;
  page: number;
  limit: number;
}

export const initialState: ProductState = {
  products: [],
  selectedProduct: null,
  loading: false,
  error: null,
  totalProducts: 0,
  page: 1,
  limit: 10
};

export const productReducer = createReducer(
  initialState,
  on(ProductActions.loadProducts, (state) => ({
    ...state,
    loading: true,
    error: null
  })),
  on(ProductActions.loadProductsSuccess, (state, { products, totalProducts }) => ({
    ...state,
    products,
    totalProducts,
    loading: false,
    error: null
  })),
  on(ProductActions.loadProductsFailure, (state, { error }) => ({
    ...state,
    loading: false,
    error
  })),
  on(ProductActions.loadProduct, (state) => ({
    ...state,
    loading: true,
    error: null
  })),
  on(ProductActions.loadProductSuccess, (state, { product }) => ({
    ...state,
    selectedProduct: product,
    loading: false,
    error: null
  })),
  on(ProductActions.loadProductFailure, (state, { error }) => ({
    ...state,
    loading: false,
    error
  })),
  on(ProductActions.searchProducts, (state) => ({
    ...state,
    loading: true,
    error: null
  })),
  on(ProductActions.searchProductsSuccess, (state, { products, totalProducts }) => ({
    ...state,
    products,
    totalProducts,
    loading: false,
    error: null
  })),
  on(ProductActions.searchProductsFailure, (state, { error }) => ({
    ...state,
    loading: false,
    error
  })),
  on(ProductActions.setPage, (state, { page }) => ({
    ...state,
    page
  })),
  on(ProductActions.setLimit, (state, { limit }) => ({
    ...state,
    limit
  }))
);