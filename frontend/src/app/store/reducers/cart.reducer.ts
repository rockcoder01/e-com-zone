import { createReducer, on } from '@ngrx/store';
import * as CartActions from '../actions/cart.actions';

export interface CartItem {
  productId: number;
  name: string;
  price: number;
  quantity: number;
  image: string;
  sku: string;
}

export interface CartState {
  items: CartItem[];
  totalItems: number;
  totalAmount: number;
  loading: boolean;
  error: string | null;
}

export const initialState: CartState = {
  items: [],
  totalItems: 0,
  totalAmount: 0,
  loading: false,
  error: null
};

export const cartReducer = createReducer(
  initialState,
  
  // Load Cart
  on(CartActions.loadCart, state => ({
    ...state,
    loading: true,
    error: null
  })),
  
  on(CartActions.loadCartSuccess, (state, { items }) => {
    const totalItems = items.reduce((sum, item) => sum + item.quantity, 0);
    const totalAmount = items.reduce((sum, item) => sum + (item.price * item.quantity), 0);
    
    return {
      ...state,
      items,
      totalItems,
      totalAmount,
      loading: false
    };
  }),
  
  on(CartActions.loadCartFailure, (state, { error }) => ({
    ...state,
    loading: false,
    error
  })),
  
  // Add to Cart
  on(CartActions.addToCart, state => ({
    ...state,
    loading: true,
    error: null
  })),
  
  on(CartActions.addToCartSuccess, (state, { item }) => {
    const existingItemIndex = state.items.findIndex(i => i.productId === item.productId);
    let updatedItems: CartItem[];
    
    if (existingItemIndex >= 0) {
      // Update existing item
      updatedItems = state.items.map((i, index) => 
        index === existingItemIndex 
          ? { ...i, quantity: i.quantity + item.quantity }
          : i
      );
    } else {
      // Add new item
      updatedItems = [...state.items, item];
    }
    
    const totalItems = updatedItems.reduce((sum, i) => sum + i.quantity, 0);
    const totalAmount = updatedItems.reduce((sum, i) => sum + (i.price * i.quantity), 0);
    
    return {
      ...state,
      items: updatedItems,
      totalItems,
      totalAmount,
      loading: false
    };
  }),
  
  on(CartActions.addToCartFailure, (state, { error }) => ({
    ...state,
    loading: false,
    error
  })),
  
  // Update Cart Item
  on(CartActions.updateCartItem, state => ({
    ...state,
    loading: true,
    error: null
  })),
  
  on(CartActions.updateCartItemSuccess, (state, { productId, quantity }) => {
    const updatedItems = state.items.map(item => 
      item.productId === productId 
        ? { ...item, quantity } 
        : item
    );
    
    const totalItems = updatedItems.reduce((sum, item) => sum + item.quantity, 0);
    const totalAmount = updatedItems.reduce((sum, item) => sum + (item.price * item.quantity), 0);
    
    return {
      ...state,
      items: updatedItems,
      totalItems,
      totalAmount,
      loading: false
    };
  }),
  
  on(CartActions.updateCartItemFailure, (state, { error }) => ({
    ...state,
    loading: false,
    error
  })),
  
  // Remove from Cart
  on(CartActions.removeFromCart, state => ({
    ...state,
    loading: true,
    error: null
  })),
  
  on(CartActions.removeFromCartSuccess, (state, { productId }) => {
    const updatedItems = state.items.filter(item => item.productId !== productId);
    const totalItems = updatedItems.reduce((sum, item) => sum + item.quantity, 0);
    const totalAmount = updatedItems.reduce((sum, item) => sum + (item.price * item.quantity), 0);
    
    return {
      ...state,
      items: updatedItems,
      totalItems,
      totalAmount,
      loading: false
    };
  }),
  
  on(CartActions.removeFromCartFailure, (state, { error }) => ({
    ...state,
    loading: false,
    error
  })),
  
  // Clear Cart
  on(CartActions.clearCart, state => ({
    ...state,
    loading: true,
    error: null
  })),
  
  on(CartActions.clearCartSuccess, state => ({
    ...initialState,
    loading: false
  })),
  
  on(CartActions.clearCartFailure, (state, { error }) => ({
    ...state,
    loading: false,
    error
  }))
);