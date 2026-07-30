import { Component, inject, OnInit } from '@angular/core';
import { ReactiveFormsModule, FormBuilder, Validators } from '@angular/forms';
import { LucideAngularModule, Mail, Lock, Eye, EyeOff, Check, ArrowRight } from 'lucide-angular';
import { AuthService } from '../../../core/services/auth.service';
import { Router, RouterLink } from '@angular/router';
import { ToastService } from '../../../core/services/toast.service';
import { ErrorService } from '../../../core/services/error.service';

@Component({
  selector: 'app-login',
  imports: [LucideAngularModule, ReactiveFormsModule, RouterLink],
  templateUrl: './login.html',
  styleUrl: './login.css',
})
export class LoginComponent implements OnInit {
  private readonly toastService = inject(ToastService);
  private readonly errorService = inject(ErrorService);
  private readonly router = inject(Router);

  passwordVisible = false;

  readonly Mail = Mail;
  readonly Lock = Lock;
  readonly Eye = Eye;
  readonly EyeOff = EyeOff;
  readonly Check = Check;
  readonly ArrowRight = ArrowRight;

  private readonly authService = inject(AuthService);
  private readonly fb = inject(FormBuilder);

  loginForm = this.fb.nonNullable.group({
    email: ['', [Validators.required, Validators.email]],

    senha: ['', [Validators.required, Validators.minLength(8)]],
  });

  get email() {
    return this.loginForm.get('email');
  }

  get senha() {
    return this.loginForm.get('senha');
  }

  login(): void {
    this.loginForm.markAllAsTouched();

    if (!this.validateForm()) {
      return;
    }

    const { email, senha } = this.loginForm.getRawValue();

    this.authService
      .login({
        email: email.trim().toLowerCase(),
        senha,
      })
      .subscribe({
        next: () => {
          this.authService.carregarUsuarioLogado().subscribe({
            next: () => {
              this.router.navigate(['/dashboard']);
            },

            error: () => {
              this.showError('Erro ao carregar usuário', 'Não foi possível carregar seus dados.');
            },
          });
        },

        error: ({ error }) => {
          const message = this.errorService.getMessage(error ?? {});

          this.showError(message.title, message.message);
        },
      });
  }

  ngOnInit() {
    const state = history.state;

    if (state.email) {
      this.loginForm.patchValue({
        email: state.email,
      });
    }
  }

  private showError(title: string, message: string): void {
    this.toastService.show({
      type: 'error',
      title,
      message,
    });
  }

  private validateForm(): boolean {
    const validations = [
      {
        valid: !this.email?.hasError('required'),
        title: 'E-mail obrigatório',
        message: 'Informe seu e-mail.',
      },

      {
        valid: !this.email?.hasError('email'),
        title: 'E-mail inválido',
        message: 'Digite um e-mail válido.',
      },

      {
        valid: !this.senha?.hasError('required'),
        title: 'Senha obrigatória',
        message: 'Informe sua senha.',
      },

      {
        valid: !this.senha?.hasError('minlength'),
        title: 'Senha inválida',
        message: 'A senha deve possuir pelo menos 8 caracteres.',
      },
    ];

    const error = validations.find((v) => !v.valid);

    if (!error) {
      return true;
    }

    this.showError(error.title, error.message);

    return false;
  }
}
