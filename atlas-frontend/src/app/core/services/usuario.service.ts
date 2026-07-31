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

  atualizarImagemPerfil(file: File) {
    const formData = new FormData();

    formData.append('file', file);

    return this.http.put<Usuario>(`${this.apiUrl}/me/imagem`, formData).pipe(
      tap((usuarioAtualizado) => {
        this.userState.setUsuario(usuarioAtualizado);
      }),
    );
  }

  removerImagemPerfil(): Observable<Usuario> {
    return this.http.delete<Usuario>(`${this.apiUrl}/me/imagem`).pipe(
      tap((usuarioAtualizado) => {
        this.userState.setUsuario(usuarioAtualizado);
      }),
    );
  }

  criarSenha(senha: string) {
    return this.http.put<void>(`${this.apiUrl}/criar-senha`, {
      senha,
    });
  }

  alterarSenha() {}

  excluirConta() {}
}
