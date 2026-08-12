import { Component, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { UserStateService } from '../../core/services/user-state.service';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [],
  templateUrl: './dashboard.html',
  styleUrl: './dashboard.css',
})
export class DashboardComponent {
  private readonly userState = inject(UserStateService);
  private readonly http = inject(HttpClient);

  protected readonly usuario = this.userState.usuario;

}
