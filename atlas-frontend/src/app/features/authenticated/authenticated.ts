import { Component, inject } from '@angular/core';
import { Router, RouterOutlet } from '@angular/router';

import { SidebarComponent } from '../../shared/sidebar/sidebar';
import { PerfilComponent } from '../perfil/perfil';
import { UserStateService } from '../../core/services/user-state.service';
import { AuthService } from '../../core/services/auth.service';

@Component({
  selector: 'app-authenticated',
  standalone: true,
  imports: [
    SidebarComponent,
    PerfilComponent,
    RouterOutlet
  ],
  templateUrl: './authenticated.html',
  styleUrl: './authenticated.css',
})
export class AuthenticatedComponent {

  private readonly authService = inject(AuthService);
  private readonly router = inject(Router);
  private readonly userState = inject(UserStateService);

  protected readonly usuario = this.userState.usuario;

  protected onLogout(): void {

    this.authService.logout();

    this.router.navigate(['/login']);
  }

}