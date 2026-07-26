import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { LoginRequest, AuthResponse, RegisterRequest } from '../../models/auth.model';

import { jwtDecode } from 'jwt-decode';

interface JwtPayload {
  exp: number;
  sub: string;
  id: number;
  role: string;
}

@Injectable({
  providedIn: 'root'
})

export class AuthService {

  private http = inject(HttpClient);

  private apiUrl = 'http://localhost:8080/auth';

  login(dados: LoginRequest): Observable<AuthResponse> {

    return this.http.post<AuthResponse>(
      `${this.apiUrl}/login`,
      dados
    );

  }

  register(dados: RegisterRequest): Observable<any> {

    return this.http.post(
      `${this.apiUrl}/register`,
      dados
    );

  }

  salvarSessao(resposta: AuthResponse) {

    localStorage.setItem(
      'accessToken',
      resposta.accessToken
    );

    localStorage.setItem(
      'refreshToken',
      resposta.refreshToken
    );

    const usuario = {
        id: resposta.id,
        nome: resposta.nome,
        email: resposta.email,
        role: resposta.role
    };

    localStorage.setItem(
        'usuario',
        JSON.stringify(usuario)
    );

  }

  getUsuario() {

    const usuario = localStorage.getItem('usuario');

    return usuario 
      ? JSON.parse(usuario)
      : null;

  }

  getToken(): string | null {

    return localStorage.getItem('accessToken');

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

  logout() {

    localStorage.removeItem('accessToken');

    localStorage.removeItem('refreshToken');

    localStorage.removeItem('usuario');

  }

}