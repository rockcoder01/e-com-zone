import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-footer',
  templateUrl: './footer.component.html',
  styleUrls: ['./footer.component.scss']
})
export class FooterComponent implements OnInit {
  currentYear: number = new Date().getFullYear();
  
  // Footer Links
  companyLinks = [
    { name: 'About Us', url: '/about' },
    { name: 'Careers', url: '/careers' },
    { name: 'Blog', url: '/blog' },
    { name: 'Press', url: '/press' },
    { name: 'Contact Us', url: '/contact' }
  ];
  
  shopLinks = [
    { name: 'All Categories', url: '/categories' },
    { name: 'Deals', url: '/deals' },
    { name: 'New Arrivals', url: '/new-arrivals' },
    { name: 'Best Sellers', url: '/bestsellers' },
    { name: 'Gift Cards', url: '/gift-cards' }
  ];
  
  helpLinks = [
    { name: 'FAQs', url: '/help/faq' },
    { name: 'Shipping', url: '/help/shipping' },
    { name: 'Returns', url: '/help/returns' },
    { name: 'Track Order', url: '/help/track-order' },
    { name: 'Size Guide', url: '/help/size-guide' }
  ];
  
  policyLinks = [
    { name: 'Terms of Service', url: '/policies/terms' },
    { name: 'Privacy Policy', url: '/policies/privacy' },
    { name: 'Refund Policy', url: '/policies/refund' },
    { name: 'Cookie Policy', url: '/policies/cookie' },
    { name: 'Accessibility', url: '/policies/accessibility' }
  ];

  constructor() { }

  ngOnInit(): void {
  }
}