import { Injectable, signal } from '@angular/core';
import { Usuario } from '../models/usuario.model';

@Injectable({
    providedIn: 'root',
})
export class UserStateService {
    private readonly _usuario = signal<Usuario | null>(null);

    readonly usuario = this._usuario.asReadonly();

    setUsuario(usuario: Usuario): void {
        this._usuario.set(usuario);
    }

    clearUsuario(): void {
        this._usuario.set(null);
    }
}
