import { Component } from '@angular/core';
import { RouterLink } from '@angular/router';
import { NavbarComponent } from '../../../features/home/navbar-component/navbar-component';
import { FooterComponent } from '../../../features/home/footer-component/footer-component';

@Component({
  selector: 'app-page404-component',
  standalone:true,
  imports: [
    RouterLink,
    FooterComponent,
    NavbarComponent
  ],
  templateUrl: './page404-component.html',
})
export class Page404Component {}
