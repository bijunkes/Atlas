import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { catchError, Observable, of, tap } from 'rxjs';
import { LoginRequest, RegisterRequest } from '../models/auth.model';

import { Usuario } from '../models/usuario.model';
import { UserStateService } from './user-state.service';
import { UsuarioService } from './usuario.service';

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  private http = inject(HttpClient);
  private userState = inject(UserStateService);
  private usuarioService = inject(UsuarioService);

  private apiUrl = 'http://localhost:8080/auth';

  login(dados: LoginRequest): Observable<Usuario> {
    return this.http.post<Usuario>(`${this.apiUrl}/login`, dados);
  }

  register(dados: RegisterRequest): Observable<any> {
    return this.http.post(`${this.apiUrl}/register`, dados);
  }

  logout(): void {
    this.http.post(`${this.apiUrl}/logout`, {}, { withCredentials: true }).subscribe({
      complete: () => {
        this.userState.clearUsuario();
      },
      error: () => {
        this.userState.clearUsuario();
      },
    });
  }

  isAuthenticated(): boolean {
    return this.userState.usuario() !== null;
  }

  carregarUsuarioLogado(): Observable<Usuario> {
    return this.usuarioService.buscarPerfil().pipe(
      tap((usuario) => {
        this.userState.setUsuario(usuario);
      }),
    );
  }

  inicializarSessao(): Observable<Usuario | null> {
    return this.carregarUsuarioLogado().pipe(
      catchError(() => {
        this.userState.clearUsuario();
        return of(null);
      }),
    );
  }

  recuperarSenha(email: string) {
    return this.http.post(`${this.apiUrl}/recuperar-senha`, {
      email,
    });
  }

  resetarSenha(dados: { token: string; novaSenha: string }) {
    return this.http.post(`${this.apiUrl}/resetar-senha`, dados);
  }

  usuarioAtual() {
    return this.userState.usuario();
  }
}
