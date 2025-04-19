import { Component, OnInit } from '@angular/core';

interface HeroSlide {
  title: string;
  subtitle: string;
  ctaText: string;
  ctaLink: string;
  imageUrl: string;
}

@Component({
  selector: 'app-hero-section',
  templateUrl: './hero-section.component.html',
  styleUrls: ['./hero-section.component.scss']
})
export class HeroSectionComponent implements OnInit {
  slides: HeroSlide[] = [
    {
      title: 'New Season Collection',
      subtitle: 'Discover the latest trends in fashion with our new arrivals.',
      ctaText: 'Shop Now',
      ctaLink: '/products',
      imageUrl: 'assets/images/hero/slide1.jpg'
    },
    {
      title: 'Special Summer Sale',
      subtitle: 'Up to 50% off on selected items. Limited time offer.',
      ctaText: 'View Deals',
      ctaLink: '/deals',
      imageUrl: 'assets/images/hero/slide2.jpg'
    },
    {
      title: 'Premium Electronics',
      subtitle: 'Explore our collection of high-quality electronic devices.',
      ctaText: 'Discover',
      ctaLink: '/categories/electronics',
      imageUrl: 'assets/images/hero/slide3.jpg'
    }
  ];
  
  currentSlide = 0;
  
  constructor() { }

  ngOnInit(): void {
    // Auto-rotate slides every 5 seconds
    setInterval(() => {
      this.nextSlide();
    }, 5000);
  }
  
  setCurrentSlide(index: number): void {
    this.currentSlide = index;
  }
  
  prevSlide(): void {
    this.currentSlide = (this.currentSlide === 0) 
      ? this.slides.length - 1 
      : this.currentSlide - 1;
  }
  
  nextSlide(): void {
    this.currentSlide = (this.currentSlide === this.slides.length - 1) 
      ? 0 
      : this.currentSlide + 1;
  }
}