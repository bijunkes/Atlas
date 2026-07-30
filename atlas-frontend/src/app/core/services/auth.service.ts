import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, tap } from 'rxjs';
import { LoginRequest, AuthResponse, RegisterRequest } from '../../models/auth.model';

import { jwtDecode } from 'jwt-decode';
import { Usuario } from '../models/usuario.model';
import { UserStateService } from './user-state.service';
import { UsuarioService } from './usuario.service';

interface JwtPayload {
  exp: number;
  sub: string;
  id: number;
  role: string;
}

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  private http = inject(HttpClient);
  private userState = inject(UserStateService);
  private usuarioService = inject(UsuarioService);

  private apiUrl = 'http://localhost:8080/auth';

  private readonly ACCESS_TOKEN_KEY = 'accessToken';
  private readonly REFRESH_TOKEN_KEY = 'refreshToken';

  login(dados: LoginRequest): Observable<AuthResponse> {
  return this.http.post<AuthResponse>(`${this.apiUrl}/login`, dados).pipe(
    tap((response) => {
      this.salvarSessao(response);
    })
  );
}

  register(dados: RegisterRequest): Observable<any> {
    return this.http.post(`${this.apiUrl}/register`, dados);
  }

  logout() {
    localStorage.removeItem(this.ACCESS_TOKEN_KEY);
    localStorage.removeItem(this.REFRESH_TOKEN_KEY);

    this.userState.clearUsuario();
  }

  getToken(): string | null {
    return localStorage.getItem(this.ACCESS_TOKEN_KEY);
  }

  isAuthenticated(): boolean {
    const token = this.getToken();

    if (!token) {
      return false;
    }

    try {
      const payload = jwtDecode<JwtPayload>(token);

      if (!payload.exp) {
        return false;
      }

      const agora = Math.floor(Date.now() / 1000);

      return payload.exp > agora;
    } catch {
      return false;
    }
  }

  salvarSessao(resposta: AuthResponse) {
    this.salvarAccessToken(resposta.accessToken);
    this.salvarRefreshToken(resposta.refreshToken);
  }

  private salvarAccessToken(token: string) {
    localStorage.setItem(this.ACCESS_TOKEN_KEY, token);
  }

  private salvarRefreshToken(token: string) {
    localStorage.setItem(this.REFRESH_TOKEN_KEY, token);
  }

  carregarUsuarioLogado(): Observable<Usuario> {
    return this.usuarioService.buscarPerfil().pipe(
      tap((usuario) => {
        this.userState.setUsuario(usuario);
      }),
    );
  }

  inicializarSessao(): void {
    if (!this.isAuthenticated()) {
      return;
    }

    this.carregarUsuarioLogado().subscribe({
      error: () => {
        this.logout();
      },
    });
  }
}
