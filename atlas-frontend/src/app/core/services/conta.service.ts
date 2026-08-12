import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

import { ContaRequest, ContaResponse } from '../models/conta.model';

@Injectable({
  providedIn: 'root',
})
export class ContaService {
  private http = inject(HttpClient);

  private apiUrl = 'http://localhost:8080/contas';

  criar(dados: ContaRequest): Observable<ContaResponse> {
    return this.http.post<ContaResponse>(this.apiUrl, dados);
  }

  listar(): Observable<ContaResponse[]> {
    return this.http.get<ContaResponse[]>(this.apiUrl);
  }

  buscarPorId(id: number): Observable<ContaResponse> {
    return this.http.get<ContaResponse>(`${this.apiUrl}/${id}`);
  }

  atualizar(
    id: number,
    dados: ContaRequest
  ): Observable<ContaResponse> {
    return this.http.put<ContaResponse>(
      `${this.apiUrl}/${id}`,
      dados
    );
  }

  desativar(id: number): Observable<void> {
    return this.http.patch<void>(
      `${this.apiUrl}/${id}/desativar`,
      {}
    );
  }

  reativar(id: number): Observable<void> {
    return this.http.patch<void>(
      `${this.apiUrl}/${id}/reativar`,
      {}
    );
  }
}