import { Component } from '@angular/core';
import { NavbarComponent } from './components/navbar/navbar';
import { HeroComponent } from './components/hero/hero';
import { BenefitsComponent } from './components/benefits/benefits';
import { FeaturesComponent } from './components/features/features';
import { CtaComponent } from './components/cta/cta';
import { FooterComponent } from './components/footer/footer';

@Component({
  selector: 'app-landing',
  imports: [
    NavbarComponent,
    HeroComponent,
    BenefitsComponent,
    FeaturesComponent,
    CtaComponent,
    FooterComponent
  ],
  templateUrl: './landing.html',
  styleUrl: './landing.css',
})
export class LandingComponent {}
