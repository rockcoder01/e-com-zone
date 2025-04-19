import { Component, OnInit } from '@angular/core';

interface CategoryCard {
  name: string;
  slug: string;
  imageUrl: string;
  itemCount: number;
}

@Component({
  selector: 'app-categories-section',
  templateUrl: './categories-section.component.html',
  styleUrls: ['./categories-section.component.scss']
})
export class CategoriesSectionComponent implements OnInit {
  categories: CategoryCard[] = [
    {
      name: 'Electronics',
      slug: 'electronics',
      imageUrl: 'assets/images/categories/electronics.jpg',
      itemCount: 256
    },
    {
      name: 'Fashion',
      slug: 'fashion',
      imageUrl: 'assets/images/categories/fashion.jpg',
      itemCount: 189
    },
    {
      name: 'Home & Kitchen',
      slug: 'home-kitchen',
      imageUrl: 'assets/images/categories/home-kitchen.jpg',
      itemCount: 143
    },
    {
      name: 'Beauty & Health',
      slug: 'beauty-health',
      imageUrl: 'assets/images/categories/beauty-health.jpg',
      itemCount: 112
    }
  ];

  constructor() { }

  ngOnInit(): void {
  }
}