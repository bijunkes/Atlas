export interface InstituicaoFinanceira {
  id: number;
  nome: string;
  codigoBanco: string;
  logo: string | null;
  ativo: boolean;
}
