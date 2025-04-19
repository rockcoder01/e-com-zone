import { createReducer, on } from '@ngrx/store';
import * as ProductActions from '../actions/product.actions';
import { Product } from '../../models/product.model';

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
  
  // Load Products
  on(ProductActions.loadProducts, (state, { page, limit }) => ({
    ...state,
    loading: true,
    error: null,
    page: page || state.page,
    limit: limit || state.limit
  })),
  
  on(ProductActions.loadProductsSuccess, (state, { products, totalProducts }) => ({
    ...state,
    products,
    totalProducts,
    loading: false
  })),
  
  on(ProductActions.loadProductsFailure, (state, { error }) => ({
    ...state,
    loading: false,
    error
  })),
  
  // Load Product
  on(ProductActions.loadProduct, state => ({
    ...state,
    selectedProduct: null,
    loading: true,
    error: null
  })),
  
  on(ProductActions.loadProductSuccess, (state, { product }) => ({
    ...state,
    selectedProduct: product,
    loading: false
  })),
  
  on(ProductActions.loadProductFailure, (state, { error }) => ({
    ...state,
    loading: false,
    error
  })),
  
  // Search Products
  on(ProductActions.searchProducts, (state, { page, limit }) => ({
    ...state,
    loading: true,
    error: null,
    page: page || state.page,
    limit: limit || state.limit
  })),
  
  on(ProductActions.searchProductsSuccess, (state, { products, totalProducts }) => ({
    ...state,
    products,
    totalProducts,
    loading: false
  })),
  
  on(ProductActions.searchProductsFailure, (state, { error }) => ({
    ...state,
    loading: false,
    error
  })),
  
  // Pagination
  on(ProductActions.setPage, (state, { page }) => ({
    ...state,
    page
  })),
  
  on(ProductActions.setLimit, (state, { limit }) => ({
    ...state,
    limit
  }))
);