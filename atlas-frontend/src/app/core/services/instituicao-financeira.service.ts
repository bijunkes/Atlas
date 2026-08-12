import { inject, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

import { InstituicaoFinanceira } from '../models/instituicao-financeira.model';

@Injectable({
  providedIn: 'root',
})
export class InstituicaoFinanceiraService {
  private readonly http = inject(HttpClient);

  private readonly apiUrl = 'http://localhost:8080/instituicoes-financeiras';

  listar(): Observable<InstituicaoFinanceira[]> {
    return this.http.get<InstituicaoFinanceira[]>(this.apiUrl);
  }
}
