import { Component, inject } from '@angular/core';
import { Router, RouterOutlet } from '@angular/router';

import { SidebarComponent } from '../../shared/sidebar/sidebar';

@Component({
  selector: 'app-categorias',
  standalone: true,
  imports: [
    SidebarComponent,
    RouterOutlet
  ],
  templateUrl: './categorias.html',
  styleUrl: './categorias.css',
})
export class CategoriasComponent {

}