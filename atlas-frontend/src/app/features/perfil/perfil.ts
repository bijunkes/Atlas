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
  X,
  Lock,
  Eye,
  EyeOff,
  Check,
} from 'lucide-angular';

import { UsuarioService } from '../../core/services/usuario.service';
import { Usuario } from '../../core/models/usuario.model';
import { AuthService } from '../../core/services/auth.service';

@Component({
  selector: 'app-perfil',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, LucideAngularModule],
  templateUrl: './perfil.html',
  styleUrl: './perfil.css',
})
export class PerfilComponent {
  private readonly fb = new FormBuilder();

  private readonly usuarioService = inject(UsuarioService);
  private readonly authService = inject(AuthService);
  private readonly toastService = inject(ToastService);

  protected readonly usuario = signal<Usuario | null>(null);
  protected readonly modalSenhaAberto = signal(false);
  protected readonly senhaVisivel = signal(false);
  protected readonly confirmacaoSenhaVisivel = signal(false);

  protected readonly icons = {
    User,
    Mail,
    CalendarDays,
    Camera,
    ShieldCheck,
    KeyRound,
    Smartphone,
    ChevronRight,
    X,
    Lock,
    Eye,
    EyeOff,
    Check,
  };

  protected readonly formulario = this.fb.nonNullable.group({
    nome: ['', Validators.required],
    email: ['', [Validators.required, Validators.email]],
  });

  protected readonly formularioSenha = this.fb.nonNullable.group({
    senha: ['', [Validators.required, Validators.minLength(8)]],

    confirmarSenha: ['', Validators.required],
  });

  constructor() {
    this.buscarUsuario();
  }

  protected get possuiSenha(): boolean {
    const provider = this.usuario()?.provider;

    return provider === 'LOCAL' || provider === 'GOOGLE_AND_LOCAL';
  }

  protected get textoSenha() {
    const provider = this.usuario()?.provider;

    switch (provider) {
      case 'GOOGLE':
        return {
          titulo: 'Criar senha',
          descricao: 'Adicione uma senha para acessar sua conta também pelo e-mail.',
        };

      case 'GOOGLE_AND_LOCAL':
        return {
          titulo: 'Alterar senha',
          descricao: 'Atualize sua senha de acesso.',
        };

      default:
        return {
          titulo: 'Alterar senha',
          descricao: 'Enviaremos um link seguro para redefinir sua senha.',
        };
    }
  }

  protected get iniciais(): string {
    const nome = this.usuario()?.nome;

    if (!nome) return '';

    return nome
      .trim()
      .split(/\s+/)
      .slice(0, 2)
      .map((parte) => parte[0]?.toUpperCase())
      .join('');
  }

  protected get dataCadastroFormatada(): string {
    const data = this.usuario()?.criadoEm;

    if (!data) return '';

    return new Intl.DateTimeFormat('pt-BR', {
      day: '2-digit',
      month: 'long',
      year: 'numeric',
    }).format(new Date(data));
  }

  protected abrirModalSenha(): void {
    this.modalSenhaAberto.set(true);
  }

  protected fecharModalSenha(): void {
    this.modalSenhaAberto.set(false);

    this.formularioSenha.reset();

    this.senhaVisivel.set(false);
    this.confirmacaoSenhaVisivel.set(false);
  }

  protected get senha() {
    return this.formularioSenha.controls.senha;
  }

  protected get confirmarSenha() {
    return this.formularioSenha.controls.confirmarSenha;
  }

  protected alternarSenha(): void {
    this.senhaVisivel.update((valor) => !valor);
  }

  protected alternarConfirmacaoSenha(): void {
    this.confirmacaoSenhaVisivel.update((valor) => !valor);
  }

  protected senhaValida(): boolean {
    return this.senha.value.length >= 8;
  }

  protected senhasIguais(): boolean {
    return this.confirmarSenha.value.length > 0 && this.senha.value === this.confirmarSenha.value;
  }

  protected criarSenha(): void {
    if (this.formularioSenha.invalid) {
      this.formularioSenha.markAllAsTouched();

      return;
    }

    const { senha, confirmarSenha } = this.formularioSenha.getRawValue();

    if (senha !== confirmarSenha) {
      this.toastService.show({
        type: 'error',
        title: 'Senhas diferentes',
        message: 'A confirmação da senha precisa ser igual.',
      });

      return;
    }

    this.usuarioService.criarSenha(senha).subscribe({
      next: () => {
        this.toastService.show({
          type: 'success',
          title: 'Senha criada',
          message: 'Agora você pode acessar sua conta usando e-mail e senha.',
        });

        this.fecharModalSenha();

        this.buscarUsuario();
      },

      error: () => {
        this.toastService.show({
          type: 'error',
          title: 'Erro',
          message: 'Não foi possível criar sua senha.',
        });
      },
    });
  }

  protected abrirGerenciamentoSenha(): void {
    if (this.usuario()?.provider === 'GOOGLE') {
      this.abrirModalSenha();

      return;
    }

    this.solicitarAlteracaoSenha();
  }

  private buscarUsuario(): void {
    this.usuarioService.buscarPerfil().subscribe({
      next: (usuario) => {
        this.usuario.set(usuario);

        this.formulario.reset({
          nome: usuario.nome,

          email: usuario.email,
        });
      },

      error: (erro) => {
        console.error('Erro ao buscar usuário:', erro);
      },
    });
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

      error: () => {
        this.toastService.show({
          type: 'error',
          title: 'Erro ao atualizar',
          message: 'Não foi possível salvar suas alterações.',
        });
      },
    });
  }

  protected cancelar(): void {
    const usuario = this.usuario();

    this.formulario.reset({
      nome: usuario?.nome ?? '',

      email: usuario?.email ?? '',
    });

    this.toastService.show({
      type: 'success',

      title: 'Alterações descartadas',

      message: 'Suas alterações foram revertidas.',
    });
  }

  protected selecionarImagem(event: Event): void {
    const input = event.target as HTMLInputElement;

    if (!input.files?.length) return;

    const arquivo = input.files[0];

    if (!arquivo.type.startsWith('image/')) {
      this.toastService.show({
        type: 'error',

        title: 'Arquivo inválido',

        message: 'Selecione apenas arquivos de imagem.',
      });

      return;
    }

    this.usuarioService.atualizarImagemPerfil(arquivo).subscribe({
      next: (usuario) => {
        this.usuario.set(usuario);

        this.toastService.show({
          type: 'success',

          title: 'Foto atualizada',

          message: 'Sua foto foi alterada com sucesso.',
        });
      },
    });
  }

  protected removerImagem(): void {
    this.usuarioService.removerImagemPerfil().subscribe({
      next: (usuario) => {
        this.usuario.set(usuario);

        this.toastService.show({
          type: 'success',

          title: 'Foto removida',

          message: 'Sua foto foi removida.',
        });
      },
    });
  }

  protected solicitarAlteracaoSenha(): void {
    const email = this.usuario()?.email;

    if (!email) return;

    this.authService.recuperarSenha(email).subscribe({
      next: () => {
        this.toastService.show({
          type: 'success',

          title: 'E-mail enviado',

          message: 'Enviamos um link para redefinir sua senha.',
        });
      },

      error: () => {
        this.toastService.show({
          type: 'error',

          title: 'Erro',

          message: 'Não foi possível enviar o e-mail.',
        });
      },
    });
  }
}
