import { Component, inject } from '@angular/core';
import { UserStateService } from '../../core/services/user-state.service';
import { AuthService } from '../../core/services/auth.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-dashboard',
  imports: [],
  templateUrl: './dashboard.html',
  styleUrl: './dashboard.css',
})
export class DashboardComponent {
  private readonly userState = inject(UserStateService);
  private readonly authService = inject(AuthService);
  private readonly router = inject(Router);

  usuario = this.userState.usuario;

  logout(): void {
    this.authService.logout();
    this.router.navigate(['/login']);
  }
}
