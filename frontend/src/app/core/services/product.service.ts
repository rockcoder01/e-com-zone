import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Product } from '../../models/product.model';
import { environment } from '../../../environments/environment';

export interface ProductsResponse {
  products: Product[];
  totalCount: number;
  page: number;
  limit: number;
}

@Injectable({
  providedIn: 'root'
})
export class ProductService {
  constructor(private http: HttpClient) {}

  getProducts(
    page = 1,
    limit = 10,
    categoryId?: number,
    sortBy = 'createdAt',
    sortDirection = 'desc'
  ): Observable<ProductsResponse> {
    let params = new HttpParams()
      .set('page', page.toString())
      .set('limit', limit.toString())
      .set('sortBy', sortBy)
      .set('sortDirection', sortDirection);

    if (categoryId) {
      params = params.set('categoryId', categoryId.toString());
    }

    return this.http.get<ProductsResponse>(
      `${environment.apiUrl}/products`,
      { params }
    );
  }

  getProductById(id: number): Observable<Product> {
    return this.http.get<Product>(`${environment.apiUrl}/products/${id}`);
  }

  searchProducts(
    query: string,
    page = 1,
    limit = 10,
    categoryId?: number,
    sortBy = 'createdAt',
    sortDirection = 'desc',
    minPrice?: number,
    maxPrice?: number
  ): Observable<ProductsResponse> {
    let params = new HttpParams()
      .set('query', query)
      .set('page', page.toString())
      .set('limit', limit.toString())
      .set('sortBy', sortBy)
      .set('sortDirection', sortDirection);

    if (categoryId) {
      params = params.set('categoryId', categoryId.toString());
    }

    if (minPrice !== undefined) {
      params = params.set('minPrice', minPrice.toString());
    }

    if (maxPrice !== undefined) {
      params = params.set('maxPrice', maxPrice.toString());
    }

    return this.http.get<ProductsResponse>(
      `${environment.apiUrl}/products/search`,
      { params }
    );
  }
}