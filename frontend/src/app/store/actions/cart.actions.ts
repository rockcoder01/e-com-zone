import { createAction, props } from '@ngrx/store';
import { CartItem } from '../reducers/cart.reducer';

// Load Cart Actions
export const loadCart = createAction(
  '[Cart] Load Cart'
);

export const loadCartSuccess = createAction(
  '[Cart] Load Cart Success',
  props<{ items: CartItem[] }>()
);

export const loadCartFailure = createAction(
  '[Cart] Load Cart Failure',
  props<{ error: string }>()
);

// Add to Cart Actions
export const addToCart = createAction(
  '[Cart] Add To Cart',
  props<{ 
    productId: number; 
    name: string;
    price: number;
    quantity: number;
    image: string;
    sku: string;
  }>()
);

export const addToCartSuccess = createAction(
  '[Cart] Add To Cart Success',
  props<{ item: CartItem }>()
);

export const addToCartFailure = createAction(
  '[Cart] Add To Cart Failure',
  props<{ error: string }>()
);

// Update Cart Item Actions
export const updateCartItem = createAction(
  '[Cart] Update Cart Item',
  props<{ productId: number; quantity: number }>()
);

export const updateCartItemSuccess = createAction(
  '[Cart] Update Cart Item Success',
  props<{ productId: number; quantity: number }>()
);

export const updateCartItemFailure = createAction(
  '[Cart] Update Cart Item Failure',
  props<{ error: string }>()
);

// Remove from Cart Actions
export const removeFromCart = createAction(
  '[Cart] Remove From Cart',
  props<{ productId: number }>()
);

export const removeFromCartSuccess = createAction(
  '[Cart] Remove From Cart Success',
  props<{ productId: number }>()
);

export const removeFromCartFailure = createAction(
  '[Cart] Remove From Cart Failure',
  props<{ error: string }>()
);

// Clear Cart Actions
export const clearCart = createAction(
  '[Cart] Clear Cart'
);

export const clearCartSuccess = createAction(
  '[Cart] Clear Cart Success'
);

export const clearCartFailure = createAction(
  '[Cart] Clear Cart Failure',
  props<{ error: string }>()
);