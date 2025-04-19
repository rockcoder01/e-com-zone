import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule, Routes } from '@angular/router';
import { ReactiveFormsModule } from '@angular/forms';

import { SharedModule } from '../../shared/shared.module';
import { ProductsComponent } from './pages/products/products.component';
import { ProductDetailComponent } from './pages/product-detail/product-detail.component';
import { ProductFilterComponent } from './components/product-filter/product-filter.component';
import { ProductGridComponent } from './components/product-grid/product-grid.component';
import { ProductCardComponent } from './components/product-card/product-card.component';
import { ProductReviewsComponent } from './components/product-reviews/product-reviews.component';

const routes: Routes = [
  {
    path: '',
    component: ProductsComponent
  },
  {
    path: ':id',
    component: ProductDetailComponent
  }
];

@NgModule({
  declarations: [
    ProductsComponent,
    ProductDetailComponent,
    ProductFilterComponent,
    ProductGridComponent,
    ProductCardComponent,
    ProductReviewsComponent
  ],
  imports: [
    CommonModule,
    ReactiveFormsModule,
    SharedModule,
    RouterModule.forChild(routes)
  ],
  exports: [
    ProductCardComponent
  ]
})
export class ProductsModule { }