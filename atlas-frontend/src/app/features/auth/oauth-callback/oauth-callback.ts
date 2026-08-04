import { Component, inject, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../../../core/services/auth.service';

@Component({
  selector: 'app-oauth-callback',
  standalone: true,
  templateUrl: './oauth-callback.html',
})
export class OAuthCallbackComponent implements OnInit {
  private router = inject(Router);
  private authService = inject(AuthService);

  ngOnInit(): void {
    this.authService.carregarUsuarioLogado().subscribe({
      next: () => {
        this.router.navigate(['/dashboard']);
      },

      error: () => {
        this.authService.logout();
        this.router.navigate(['/login']);
      },
    });
  }
}
