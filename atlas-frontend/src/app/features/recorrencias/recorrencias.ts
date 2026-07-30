import { Component, inject } from '@angular/core';
import { Router, RouterOutlet } from '@angular/router';

import { SidebarComponent } from '../../shared/sidebar/sidebar';

@Component({
  selector: 'app-recorrencias',
  standalone: true,
  imports: [
    SidebarComponent,
    RouterOutlet
  ],
  templateUrl: './recorrencias.html',
  styleUrl: './recorrencias.css',
})
export class RecorrenciasComponent {

}