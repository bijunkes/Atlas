import { Injectable, signal } from '@angular/core';
import { Usuario } from '../models/usuario.model';

@Injectable({
    providedIn: 'root',
})
export class UserStateService {
    // Estado privado, somente a service pode alterá-lo
    private readonly _usuario = signal<Usuario | null>(null);

    // Permite que qualquer componente leia o usuário, sem modificá-lo
    readonly usuario = this._usuario.asReadonly();

    setUsuario(usuario: Usuario): void {
        this._usuario.set(usuario);
    }

    clearUsuario(): void {
        this._usuario.set(null);
    }
}
