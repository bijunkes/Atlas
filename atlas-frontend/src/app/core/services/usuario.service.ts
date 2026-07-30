import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, tap } from 'rxjs';

import { Usuario } from '../models/usuario.model';
import { UserStateService } from './user-state.service';

@Injectable({
  providedIn: 'root',
})
export class UsuarioService {
  private http = inject(HttpClient);
  private userState = inject(UserStateService);

  private apiUrl = 'http://localhost:8080/usuarios';

  buscarPerfil(): Observable<Usuario> {
    return this.http.get<Usuario>(`${this.apiUrl}/me`);
  }

  atualizarPerfil(dados: { nome: string; email: string }): Observable<Usuario> {
    return this.http.put<Usuario>(`${this.apiUrl}/me`, dados).pipe(
      tap((usuarioAtualizado) => {
        this.userState.setUsuario(usuarioAtualizado);
      }),
    );
  }

  alterarSenha() {}

  excluirConta() {}
}
