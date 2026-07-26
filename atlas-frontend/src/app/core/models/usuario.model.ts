export interface Usuario {
    id: number;
    nome: string;
    email: string;
    role: 'USER' | 'ADMIN';
}