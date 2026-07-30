import { Component, inject } from '@angular/core';
import { Router, RouterOutlet } from '@angular/router';

import { SidebarComponent } from '../../shared/sidebar/sidebar';

@Component({
  selector: 'app-transacoes',
  standalone: true,
  imports: [
    SidebarComponent,
    RouterOutlet
  ],
  templateUrl: './transacoes.html',
  styleUrl: './transacoes.css',
})
export class TransacoesComponent {

}