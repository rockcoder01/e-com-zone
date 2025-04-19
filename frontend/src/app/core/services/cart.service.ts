import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, of } from 'rxjs';
import { map, tap } from 'rxjs/operators';
import { environment } from '../../../environments/environment';
import { CartItem } from '../../store/reducers/cart.reducer';
import { AuthService } from './auth.service';

interface CartResponseItem {
  productId: number;
  name: string;
  price: number;
  quantity: number;
  image: string;
  sku: string;
}

interface CartResponse {
  items: CartResponseItem[];
  totalItems: number;
  totalAmount: number;
}

@Injectable({
  providedIn: 'root'
})
export class CartService {
  private localCartKey = 'local_cart';

  constructor(
    private http: HttpClient,
    private authService: AuthService
  ) {}

  getCart(): Observable<CartItem[]> {
    if (this.authService.isAuthenticated()) {
      return this.http.get<CartResponse>(`${environment.apiUrl}/cart`).pipe(
        map(response => response.items)
      );
    } else {
      // For unauthenticated users, get cart from local storage
      const localCartItems = this.getLocalCart();
      return of(localCartItems);
    }
  }

  addToCart(productId: number, quantity: number): Observable<any> {
    if (this.authService.isAuthenticated()) {
      return this.http.post(`${environment.apiUrl}/cart/items`, {
        productId,
        quantity
      });
    } else {
      // For unauthenticated users, add to local storage cart
      return this.addToLocalCart(productId, quantity);
    }
  }

  updateCartItem(productId: number, quantity: number): Observable<any> {
    if (this.authService.isAuthenticated()) {
      return this.http.put(`${environment.apiUrl}/cart/items/${productId}`, {
        quantity
      });
    } else {
      // For unauthenticated users, update in local storage cart
      return this.updateLocalCartItem(productId, quantity);
    }
  }

  removeFromCart(productId: number): Observable<any> {
    if (this.authService.isAuthenticated()) {
      return this.http.delete(`${environment.apiUrl}/cart/items/${productId}`);
    } else {
      // For unauthenticated users, remove from local storage cart
      return this.removeFromLocalCart(productId);
    }
  }

  clearCart(): Observable<any> {
    if (this.authService.isAuthenticated()) {
      return this.http.delete(`${environment.apiUrl}/cart`);
    } else {
      // For unauthenticated users, clear local storage cart
      return this.clearLocalCart();
    }
  }

  // Local cart methods for unauthenticated users
  private getLocalCart(): CartItem[] {
    const cartString = localStorage.getItem(this.localCartKey);
    return cartString ? JSON.parse(cartString) : [];
  }

  private saveLocalCart(cart: CartItem[]): void {
    localStorage.setItem(this.localCartKey, JSON.stringify(cart));
  }

  private addToLocalCart(productId: number, quantity: number): Observable<any> {
    // In a real app, you would fetch the product details from an API
    // This is a simplified implementation
    const cart = this.getLocalCart();
    const existingItemIndex = cart.findIndex(item => item.productId === productId);

    if (existingItemIndex >= 0) {
      cart[existingItemIndex].quantity += quantity;
    } else {
      // You would need to get these details from an API call in a real app
      cart.push({
        productId,
        name: 'Sample Product',
        price: 0,
        quantity,
        image: 'assets/images/product-placeholder.png',
        sku: 'SKU-' + productId
      });
    }

    this.saveLocalCart(cart);
    return of({ success: true });
  }

  private updateLocalCartItem(productId: number, quantity: number): Observable<any> {
    const cart = this.getLocalCart();
    const existingItemIndex = cart.findIndex(item => item.productId === productId);

    if (existingItemIndex >= 0) {
      cart[existingItemIndex].quantity = quantity;
      this.saveLocalCart(cart);
    }

    return of({ success: true });
  }

  private removeFromLocalCart(productId: number): Observable<any> {
    const cart = this.getLocalCart();
    const updatedCart = cart.filter(item => item.productId !== productId);
    this.saveLocalCart(updatedCart);
    return of({ success: true });
  }

  private clearLocalCart(): Observable<any> {
    localStorage.removeItem(this.localCartKey);
    return of({ success: true });
  }

  // Method to sync local cart with server when user logs in
  syncLocalCartWithServer(): Observable<any> {
    const localCart = this.getLocalCart();
    
    if (localCart.length === 0) {
      return of({ success: true });
    }

    // Convert local cart to format expected by API
    const cartItems = localCart.map(item => ({
      productId: item.productId,
      quantity: item.quantity
    }));

    return this.http.post(`${environment.apiUrl}/cart/sync`, { items: cartItems }).pipe(
      tap(() => {
        // Clear local cart after successful sync
        this.clearLocalCart();
      })
    );
  }
}