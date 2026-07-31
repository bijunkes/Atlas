export type AuthProvider = 
  | 'LOCAL'
  | 'GOOGLE'
  | 'GOOGLE_AND_LOCAL';

export interface Usuario {
  id: number;
  nome: string;
  email: string;
  role: string;
  criadoEm: string;
  imagemPerfil: string | null;
  provider: AuthProvider;
}