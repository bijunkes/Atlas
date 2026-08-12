import { TipoConta } from "./tipo-conta.model";

export interface ContaRequest {
  instituicaoId?: number | null;
  nome: string;
  tipo: TipoConta;
  saldoInicial: number;
  numeroAgencia?: string | null;
  numeroConta?: string | null;
}

export interface ContaResponse {
  id: number;
  nome: string;
  tipo: TipoConta;
  saldoAtual: number;
  instituicaoNome: string | null;
  ativo: boolean;
}