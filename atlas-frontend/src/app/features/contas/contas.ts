import { Component, inject } from '@angular/core';
import { Router, RouterOutlet } from '@angular/router';

import { SidebarComponent } from '../../shared/sidebar/sidebar';

@Component({
  selector: 'app-contas',
  standalone: true,
  imports: [
    SidebarComponent,
    RouterOutlet
  ],
  templateUrl: './contas.html',
  styleUrl: './contas.css',
})
export class ContasComponent {

}