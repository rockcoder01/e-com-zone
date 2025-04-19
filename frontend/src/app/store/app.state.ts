import { AuthState } from './reducers/auth.reducer';
import { ProductState } from './reducers/product.reducer';
import { CartState } from './reducers/cart.reducer';

export interface AppState {
  auth: AuthState;
  products: ProductState;
  cart: CartState;
}