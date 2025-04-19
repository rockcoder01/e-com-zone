import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule, Routes } from '@angular/router';

import { SharedModule } from '../../shared/shared.module';
import { HomeComponent } from './home.component';
import { HeroSectionComponent } from './components/hero-section/hero-section.component';
import { FeaturedProductsComponent } from './components/featured-products/featured-products.component';
import { CategoriesSectionComponent } from './components/categories-section/categories-section.component';
import { PromotionsSectionComponent } from './components/promotions-section/promotions-section.component';

const routes: Routes = [
  {
    path: '',
    component: HomeComponent
  }
];

@NgModule({
  declarations: [
    HomeComponent,
    HeroSectionComponent,
    FeaturedProductsComponent,
    CategoriesSectionComponent,
    PromotionsSectionComponent
  ],
  imports: [
    CommonModule,
    SharedModule,
    RouterModule.forChild(routes)
  ]
})
export class HomeModule { }