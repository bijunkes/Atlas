import { Component, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { ToastService } from '../../core/services/toast.service';

import {
  LucideAngularModule,
  User,
  Mail,
  CalendarDays,
  Camera,
  ShieldCheck,
  KeyRound,
  Smartphone,
  ChevronRight,
} from 'lucide-angular';

import { UsuarioService } from '../../core/services/usuario.service';
import { Usuario } from '../../core/models/usuario.model';

export interface UsuarioPerfil {
  id: number;
  nome: string;
  email: string;
  criadoEm: string;
  avatarUrl?: string | null;
}

@Component({
  selector: 'app-perfil',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, LucideAngularModule],
  templateUrl: './perfil.html',
  styleUrl: './perfil.css',
})
export class PerfilComponent {
  private readonly usuarioService = inject(UsuarioService);

  protected readonly usuario = signal<Usuario | null>(null);

  private readonly fb = new FormBuilder();

  private readonly toastService = inject(ToastService);

  protected readonly icons = {
    User,
    Mail,
    CalendarDays,
    Camera,
    ShieldCheck,
    KeyRound,
    Smartphone,
    ChevronRight,
  };

  protected readonly formulario = this.fb.nonNullable.group({
    nome: ['', Validators.required],

    email: ['', [Validators.required, Validators.email]],
  });

  constructor() {
    this.carregarUsuario();
  }

  private carregarUsuario(): void {
    this.usuarioService.buscarPerfil().subscribe({
      next: (usuario) => {
        this.usuario.set(usuario);

        this.formulario.reset({
          nome: usuario.nome,

          email: usuario.email,
        });
      },

      error: (erro) => {
        console.error('Erro ao carregar perfil:', erro);
      },
    });
  }

  protected get iniciais(): string {
    const nome = this.usuario()?.nome;

    if (!nome) {
      return '';
    }

    return nome
      .trim()
      .split(/\s+/)
      .slice(0, 2)
      .map((parte) => parte[0]?.toUpperCase())
      .join('');
  }

  protected get dataCadastroFormatada(): string {
    const data = this.usuario()?.criadoEm;

    if (!data) {
      return '';
    }

    return new Intl.DateTimeFormat('pt-BR', {
      day: '2-digit',
      month: 'long',
      year: 'numeric',
    }).format(new Date(data));
  }

  protected enviarFormulario(): void {
    if (this.formulario.invalid) {
      this.formulario.markAllAsTouched();
      return;
    }

    this.usuarioService.atualizarPerfil(this.formulario.getRawValue()).subscribe({
      next: () => {
        this.toastService.show({
          type: 'success',
          title: 'Perfil atualizado',
          message: 'Suas informações foram atualizadas com sucesso.',
        });
      },

      error: (erro) => {
        console.error('Erro ao atualizar perfil:', erro);

        this.toastService.show({
          type: 'error',
          title: 'Erro ao atualizar',
          message: 'Não foi possível salvar suas alterações.',
        });
      },
    });
  }

  protected cancelar(): void {
    const usuarioAtual = this.usuario();

    this.formulario.reset({
      nome: usuarioAtual?.nome ?? '',

      email: usuarioAtual?.email ?? '',
    });
  }

  protected abrirAlteracaoAvatar(): void {
    console.log('Alterar avatar');
  }
}
