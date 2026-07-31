import { Component, inject, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { AuthService } from '../../../core/services/auth.service';

@Component({
  selector: 'app-oauth-callback',
  standalone: true,
  templateUrl: './oauth-callback.html',
})
export class OAuthCallbackComponent implements OnInit {
  private route = inject(ActivatedRoute);
  private router = inject(Router);
  private authService = inject(AuthService);

  ngOnInit(): void {
    const accessToken = this.route.snapshot.queryParamMap.get('accessToken');

    const refreshToken = this.route.snapshot.queryParamMap.get('refreshToken');

    if (!accessToken || !refreshToken) {
      this.router.navigate(['/login']);
      return;
    }

    this.authService.salvarTokens(accessToken, refreshToken);

    this.authService.carregarUsuarioLogado().subscribe({
      next: (usuario) => {
        this.router.navigate(['/dashboard']);
      },

      error: (erro) => {
        this.authService.logout();
        this.router.navigate(['/login']);
      },
    });
  }
}
