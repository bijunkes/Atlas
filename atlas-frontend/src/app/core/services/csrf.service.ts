import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, map } from 'rxjs';

interface CsrfResponse {
  token: string;
}

@Injectable({
  providedIn: 'root',
})
export class CsrfService {
  private readonly http = inject(HttpClient);

  obterToken(): Observable<string> {
    return this.http
      .get<CsrfResponse>('http://localhost:8080/csrf', {
        withCredentials: true,
      })
      .pipe(
        map((resposta) => resposta.token)
      );
  }

  inicializar(): Observable<string> {
    return this.obterToken();
  }

  limparToken(): void {
  }
}