export interface Product {
  id: number;
  name: string;
  description: string;
  price: number;
  originalPrice?: number;
  stock: number;
  sku: string;
  images: string[];
  categories: Category[];
  rating: number;
  reviewCount: number;
  isInWishlist?: boolean;
  vendor?: Vendor;
  attributes?: ProductAttribute[];
  tags?: string[];
  createdAt: Date;
  updatedAt: Date;
  isActive: boolean;
}

export interface Category {
  id: number;
  name: string;
  slug: string;
  parentId?: number;
}

export interface Vendor {
  id: number;
  name: string;
  slug: string;
  logoUrl?: string;
  rating?: number;
}

export interface ProductAttribute {
  id: number;
  name: string;
  value: string;
}