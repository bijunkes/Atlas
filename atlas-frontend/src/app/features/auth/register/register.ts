import { Component, inject, signal } from '@angular/core';
import { HttpErrorResponse } from '@angular/common/http';
import {
  LucideAngularModule,
  Mail,
  Lock,
  Eye,
  EyeOff,
  Check,
  ArrowRight,
  User,
  ShieldCheck,
} from 'lucide-angular';
import { ReactiveFormsModule, FormBuilder, Validators } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';

import { AuthService } from '../../../core/services/auth.service';
import { ToastService } from '../../../core/services/toast.service';
import { passwordMatchValidator } from '../../../core/validators/password-match.validator';

interface FieldErrorRule {
  invalid: boolean;
  title: string;
  message: string;
}

@Component({
  selector: 'app-register',
  imports: [LucideAngularModule, ReactiveFormsModule, RouterLink],
  templateUrl: './register.html',
  styleUrl: './register.css',
})
export class RegisterComponent {
  private readonly fb = inject(FormBuilder);
  private readonly authService = inject(AuthService);
  private readonly toastService = inject(ToastService);
  private readonly router = inject(Router);

  readonly User = User;
  readonly Mail = Mail;
  readonly Lock = Lock;
  readonly Eye = Eye;
  readonly EyeOff = EyeOff;
  readonly Check = Check;
  readonly ArrowRight = ArrowRight;
  readonly ShieldCheck = ShieldCheck;

  passwordVisible = false;
  confirmPasswordVisible = false;
  isSubmitting = signal(false);

  protected readonly googleAuthUrl = 'http://localhost:8080/oauth2/authorization/google';

  readonly registerForm = this.fb.nonNullable.group(
    {
      nome: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      senha: ['', [Validators.required, Validators.minLength(8)]],
      confirmarSenha: ['', Validators.required],
      termosAceitos: [false, Validators.requiredTrue],
    },
    {
      validators: passwordMatchValidator('senha', 'confirmarSenha'),
    },
  );

  get nome() {
    return this.registerForm.controls.nome;
  }
  get email() {
    return this.registerForm.controls.email;
  }
  get senha() {
    return this.registerForm.controls.senha;
  }
  get confirmarSenha() {
    return this.registerForm.controls.confirmarSenha;
  }
  get termosAceitos() {
    return this.registerForm.controls.termosAceitos;
  }

  register(): void {
    this.registerForm.markAllAsTouched();

    if (!this.isFormValid()) {
      return;
    }

    const { nome, email, senha } = this.registerForm.getRawValue();
    this.isSubmitting.set(true);

    this.authService
      .register({
        nome: nome.trim(),
        email: email.trim().toLowerCase(),
        senha,
      })
      .subscribe({
        next: () => {
          this.isSubmitting.set(false);

          this.router.navigate(['/login'], {
            state: {
              email,
              registered: true,
            },
          });
        },
        error: (err: HttpErrorResponse) => {
          this.isSubmitting.set(false);

          if (err.status === 409) {
            this.showError('E-mail já cadastrado', 'Já existe uma conta usando esse e-mail.');
            return;
          }

          if (err.status === 400) {
            this.showError('Dados inválidos', 'Verifique as informações preenchidas.');
            return;
          }

          this.showError(
            'Erro ao criar conta',
            'Não foi possível criar sua conta. Tente novamente.',
          );
        },
      });
  }

  private isFormValid(): boolean {
    const rules: FieldErrorRule[] = [
      {
        invalid: this.nome.invalid,
        title: 'Nome obrigatório',
        message: 'Informe seu nome completo.',
      },
      {
        invalid: this.email.hasError('required'),
        title: 'E-mail obrigatório',
        message: 'Informe seu e-mail.',
      },
      {
        invalid: this.email.hasError('email'),
        title: 'E-mail inválido',
        message: 'Digite um e-mail válido.',
      },
      {
        invalid: this.senha.hasError('required'),
        title: 'Senha obrigatória',
        message: 'Informe uma senha.',
      },
      {
        invalid: this.senha.hasError('minlength'),
        title: 'Senha muito curta',
        message: 'A senha deve possuir pelo menos 8 caracteres.',
      },
      {
        invalid: this.registerForm.hasError('passwordMismatch'),
        title: 'Senhas diferentes',
        message: 'As senhas devem coincidir.',
      },
      {
        invalid: this.termosAceitos.invalid,
        title: 'Termos',
        message: 'Aceite os termos de uso.',
      },
    ];

    const firstError = rules.find((rule) => rule.invalid);

    if (firstError) {
      this.showError(firstError.title, firstError.message);
      return false;
    }

    return true;
  }

  private showError(title: string, message: string): void {
    this.toastService.show({ type: 'error', title, message });
  }

  private showSuccess(title: string, message: string): void {
    this.toastService.show({
      type: 'success',
      title,
      message,
    });
  }
}
