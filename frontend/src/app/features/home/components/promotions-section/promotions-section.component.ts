import { Component, OnInit } from '@angular/core';

interface Promotion {
  title: string;
  description: string;
  imageUrl: string;
  linkUrl: string;
  buttonText: string;
  backgroundColor: string;
}

@Component({
  selector: 'app-promotions-section',
  templateUrl: './promotions-section.component.html',
  styleUrls: ['./promotions-section.component.scss']
})
export class PromotionsSectionComponent implements OnInit {
  promotions: Promotion[] = [
    {
      title: 'Summer Collection 2025',
      description: 'Get ready for summer with our latest collection. Up to 30% off on selected items.',
      imageUrl: 'assets/images/promotions/summer-collection.jpg',
      linkUrl: '/collections/summer-2025',
      buttonText: 'Shop Collection',
      backgroundColor: '#f8f1e7'
    },
    {
      title: 'New Electronics',
      description: 'Discover the latest tech gadgets. Free shipping on all electronics.',
      imageUrl: 'assets/images/promotions/electronics-promo.jpg',
      linkUrl: '/categories/electronics',
      buttonText: 'Explore Now',
      backgroundColor: '#e7f0f8'
    }
  ];

  constructor() { }

  ngOnInit(): void {
  }
}