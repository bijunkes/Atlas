import { Component, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { toSignal } from '@angular/core/rxjs-interop';
import { map } from 'rxjs';
import {
  LucideAngularModule,
  Lock,
  ShieldCheck,
  Eye,
  EyeOff,
  Check,
  ArrowRight,
  Globe,
} from 'lucide-angular';
import { AuthService } from '../../../core/services/auth.service';
import { ToastService } from '../../../core/services/toast.service';
import { Router } from '@angular/router';

import { passwordMatchValidator } from '../../../core/validators/password-match.validator';

@Component({
  selector: 'app-reset-password',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterLink, LucideAngularModule],
  templateUrl: './reset-password.html',
})
export class ResetPasswordComponent {
  private readonly fb = inject(FormBuilder);
  private readonly route = inject(ActivatedRoute);

  private readonly authService = inject(AuthService);
  private readonly toastService = inject(ToastService);
  private readonly router = inject(Router);

  protected readonly icons = { Lock, ShieldCheck, Eye, EyeOff, Check, ArrowRight, Globe };

  protected readonly token = toSignal(
    this.route.queryParamMap.pipe(map((params) => params.get('token'))),
    { initialValue: null },
  );

  protected readonly passwordVisible = signal(false);
  protected readonly confirmPasswordVisible = signal(false);
  protected readonly isSubmitting = signal(false);

  protected togglePasswordVisibility(): void {
    this.passwordVisible.update((visible) => !visible);
  }

  protected toggleConfirmPasswordVisibility(): void {
    this.confirmPasswordVisible.update((visible) => !visible);
  }

  readonly form = this.fb.nonNullable.group(
    {
      senha: ['', [Validators.required, Validators.minLength(8)]],
      confirmarSenha: ['', Validators.required],
    },
    { validators: passwordMatchValidator('senha', 'confirmarSenha') },
  );

  get senha() {
    return this.form.controls.senha;
  }

  get confirmarSenha() {
    return this.form.controls.confirmarSenha;
  }

  protected readonly hasMinLength = toSignal(
    this.form.controls.senha.valueChanges.pipe(map((value) => (value ?? '').length >= 8)),
    { initialValue: false },
  );

  protected readonly passwordsMatch = toSignal(
    this.form.valueChanges.pipe(
      map(() => {
        const { senha, confirmarSenha } = this.form.getRawValue();
        return senha.length > 0 && senha === confirmarSenha;
      }),
    ),
    { initialValue: false },
  );

  protected onSubmit(): void {
    this.form.markAllAsTouched();

    if (this.form.invalid) {
      return;
    }

    const token = this.token();

    if (!token) {
      this.toastService.show({
        type: 'error',
        title: 'Link inválido',
        message: 'Não encontramos o token de recuperação.',
      });

      return;
    }

    this.isSubmitting.set(true);

    this.authService
      .resetarSenha({
        token,
        novaSenha: this.senha.value,
      })
      .subscribe({
        next: () => {
          this.toastService.show({
            type: 'success',
            title: 'Senha alterada',
            message: 'Sua senha foi redefinida com sucesso.',
          });

          this.router.navigate(['/login']);
        },

        error: (erro) => {
          this.isSubmitting.set(false);

          this.toastService.show({
            type: 'error',
            title: 'Erro ao alterar senha',
            message: erro.error?.message ?? 'O link pode ter expirado.',
          });
        },
      });
  }
}
