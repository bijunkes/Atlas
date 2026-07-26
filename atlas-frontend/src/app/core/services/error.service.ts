import { Injectable } from '@angular/core';
import { ErrorResponse } from '../../models/error-response.model';

export interface ErrorMessage {
    title: string;
    message: string;
}

@Injectable({
    providedIn: 'root',
})
export class ErrorService {
    getMessage(error: ErrorResponse): ErrorMessage {
        switch (error?.code) {
        case 'INVALID_CREDENTIALS':
            return {
            title: 'Login inválido',
            message: 'E-mail ou senha incorretos.',
            };

        case 'EMAIL_ALREADY_EXISTS':
            return {
            title: 'E-mail já cadastrado',
            message: 'Este e-mail já possui uma conta.',
            };

        case 'VALIDATION_ERROR':
            return {
            title: 'Dados inválidos',
            message: error.message,
            };

        case 'TOKEN_EXPIRED':
            return {
            title: 'Sessão expirada',
            message: 'Sua sessão expirou. Faça login novamente.',
            };

        case 'TOKEN_INVALID':
            return {
            title: 'Sessão inválida',
            message: 'Sua sessão é inválida. Faça login novamente.',
            };

        case 'UNAUTHORIZED':
            return {
            title: 'Não autenticado',
            message: 'Você precisa estar autenticado para acessar este recurso.',
            };

        case 'ACCESS_DENIED':
            return {
            title: 'Acesso negado',
            message: 'Você não possui permissão para acessar este recurso.',
            };

        case 'INTERNAL_SERVER_ERROR':
            return {
            title: 'Erro no servidor',
            message: 'Ocorreu um erro inesperado. Tente novamente mais tarde.',
            };

        default:
            return {
            title: 'Erro inesperado',
            message: 'Não foi possível completar a operação.',
            };
        }
    }
}
