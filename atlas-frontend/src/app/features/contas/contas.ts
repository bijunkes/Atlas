import { Component, computed, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { finalize } from 'rxjs';

import {
  LucideAngularModule,
  Plus,
  MoreVertical,
  Pencil,
  Eye,
  Power,
  X,
  AlertTriangle,
  Wallet,
  Landmark,
  PiggyBank,
  TrendingUp,
  type LucideIconData,
} from 'lucide-angular';

import { ContaService } from '../../core/services/conta.service';
import { ToastService } from '../../core/services/toast.service';
import { ErrorService } from '../../core/services/error.service';

import { ContaRequest, ContaResponse } from '../../core/models/conta.model';

import { TipoConta } from '../../core/models/tipo-conta.model';

import { InstituicaoFinanceiraService } from '../../core/services/instituicao-financeira.service';

import { InstituicaoFinanceira } from '../../core/models/instituicao-financeira.model';

@Component({
  selector: 'app-contas',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, LucideAngularModule],
  templateUrl: './contas.html',
  styleUrl: './contas.css',
})
export class ContasComponent {
  private readonly fb = inject(FormBuilder);
  private readonly contaService = inject(ContaService);
  private readonly toastService = inject(ToastService);
  private readonly errorService = inject(ErrorService);
  private readonly instituicaoFinanceiraService = inject(InstituicaoFinanceiraService);

  protected readonly TipoConta = TipoConta;

  protected readonly instituicoes = signal<InstituicaoFinanceira[]>([]);

  protected readonly carregandoInstituicoes = signal(false);

  protected readonly icons = {
    Plus,
    MoreVertical,
    Pencil,
    Eye,
    Power,
    X,
    AlertTriangle,
    Wallet,
  };

  private readonly iconesPorTipo: Record<TipoConta, LucideIconData> = {
    [TipoConta.CARTEIRA]: Wallet,
    [TipoConta.CONTA_CORRENTE]: Landmark,
    [TipoConta.POUPANCA]: PiggyBank,
    [TipoConta.INVESTIMENTO]: TrendingUp,
  };

  private readonly rotulosPorTipo: Record<TipoConta, string> = {
    [TipoConta.CARTEIRA]: 'Carteira',
    [TipoConta.CONTA_CORRENTE]: 'Conta corrente',
    [TipoConta.POUPANCA]: 'Poupança',
    [TipoConta.INVESTIMENTO]: 'Investimento',
  };

  protected readonly tiposConta = Object.values(TipoConta);

  // ---------- Estado da lista ----------

  protected readonly contas = signal<ContaResponse[]>([]);

  protected readonly carregando = signal(true);

  protected readonly erro = signal(false);

  // ---------- Estado do menu ----------

  protected readonly menuAberto = signal<number | null>(null);

  // ---------- Estado do modal criar/editar ----------

  protected readonly modalAberto = signal(false);

  protected readonly contaEmEdicao = signal<ContaResponse | null>(null);

  protected readonly enviando = signal(false);

  // ---------- Estado do modal de desativação ----------

  protected readonly contaParaDesativar = signal<ContaResponse | null>(null);

  protected readonly desativando = signal(false);

  // ---------- Computed ----------

  protected readonly saldoTotal = computed(() =>
    this.contas().reduce((total, conta) => total + conta.saldoAtual, 0),
  );

  protected readonly contasAtivas = computed(
    () => this.contas().filter((conta) => conta.ativo).length,
  );

  // ---------- Formulário ----------

  protected readonly formulario = this.fb.nonNullable.group({
    nome: ['', [Validators.required, Validators.maxLength(100)]],

    tipo: [TipoConta.CONTA_CORRENTE, Validators.required],

    instituicaoId: [null as number | null],

    numeroAgencia: [''],

    numeroConta: [''],

    saldoInicial: [0, [Validators.required]],
  });

  constructor() {
    this.carregarContas();
    this.carregarInstituicoes();

    this.formulario.controls.tipo.valueChanges.subscribe((tipo) => {
      this.atualizarValidadoresPorTipo(tipo);
    });
  }

  // ---------- Lista ----------

  protected carregarContas(): void {
    this.carregando.set(true);
    this.erro.set(false);

    this.contaService
      .listar()
      .pipe(finalize(() => this.carregando.set(false)))
      .subscribe({
        next: (contas) => {
          this.contas.set(contas);
        },

        error: () => {
          this.erro.set(true);
        },
      });
  }

  protected carregarInstituicoes(): void {
  this.carregandoInstituicoes.set(true);

  this.instituicaoFinanceiraService
    .listar()
    .pipe(
      finalize(() =>
        this.carregandoInstituicoes.set(false),
      ),
    )
    .subscribe({
      next: (instituicoes) => {
        this.instituicoes.set(instituicoes);
      },

      error: () => {
        this.toastService.show({
          type: 'error',
          title: 'Erro',
          message:
            'Não foi possível carregar as instituições financeiras.',
        });
      },
    });
}

  // ---------- Exibição ----------

  protected iconePorTipo(tipo: TipoConta): LucideIconData {
    return this.iconesPorTipo[tipo];
  }

  protected rotuloTipo(tipo: TipoConta): string {
    return this.rotulosPorTipo[tipo];
  }

  protected formatarMoeda(valor: number): string {
    return new Intl.NumberFormat('pt-BR', {
      style: 'currency',
      currency: 'BRL',
    }).format(valor ?? 0);
  }

  protected get mostrarInstituicao(): boolean {
    return this.formulario.controls.tipo.value !== TipoConta.CARTEIRA;
  }

  protected get mostrarAgenciaConta(): boolean {
    const tipo = this.formulario.controls.tipo.value;

    return tipo === TipoConta.CONTA_CORRENTE || tipo === TipoConta.POUPANCA;
  }

  // ---------- Menu de ações ----------

  protected alternarMenu(id: number, event: Event): void {
    event.stopPropagation();

    this.menuAberto.set(this.menuAberto() === id ? null : id);
  }

  protected fecharMenu(): void {
    this.menuAberto.set(null);
  }

  protected verDetalhes(conta: ContaResponse): void {
    this.fecharMenu();

    this.toastService.show({
      type: 'info',
      title: conta.nome,
      message: 'O detalhamento completo da conta estará disponível em breve.',
    });
  }

  // ---------- Modal criar/editar ----------

  protected abrirModalNovaConta(): void {
    this.contaEmEdicao.set(null);

    this.formulario.reset({
      nome: '',
      tipo: TipoConta.CONTA_CORRENTE,
      instituicaoId: null,
      numeroAgencia: '',
      numeroConta: '',
      saldoInicial: 0,
    });

    this.atualizarValidadoresPorTipo(TipoConta.CONTA_CORRENTE);

    this.modalAberto.set(true);
  }

  protected editarConta(conta: ContaResponse): void {
    this.fecharMenu();

    this.contaEmEdicao.set(conta);

    this.formulario.reset({
      nome: conta.nome,
      tipo: conta.tipo,

      // O ContaResponse atual não possui
      // instituicaoId, numeroAgencia ou numeroConta.
      instituicaoId: null,
      numeroAgencia: '',
      numeroConta: '',

      saldoInicial: conta.saldoAtual,
    });

    this.atualizarValidadoresPorTipo(conta.tipo);

    this.modalAberto.set(true);
  }

  protected fecharModal(): void {
    if (this.enviando()) return;

    this.modalAberto.set(false);
    this.contaEmEdicao.set(null);
  }

  // ---------- Salvar ----------

  protected salvar(): void {
    if (this.formulario.invalid) {
      this.formulario.markAllAsTouched();
      return;
    }

    const bruto = this.formulario.getRawValue();

    const dados: ContaRequest = {
      nome: bruto.nome.trim(),

      tipo: bruto.tipo,

      instituicaoId: this.mostrarInstituicao ? bruto.instituicaoId : null,

      numeroAgencia: this.mostrarAgenciaConta ? bruto.numeroAgencia.trim() || null : null,

      numeroConta: this.mostrarAgenciaConta ? bruto.numeroConta.trim() || null : null,

      saldoInicial: Number(bruto.saldoInicial),
    };

    const contaEmEdicao = this.contaEmEdicao();

    this.enviando.set(true);

    const requisicao$ = contaEmEdicao
      ? this.contaService.atualizar(contaEmEdicao.id, dados)
      : this.contaService.criar(dados);

    requisicao$.pipe(finalize(() => this.enviando.set(false))).subscribe({
      next: () => {
        this.toastService.show({
          type: 'success',
          title: contaEmEdicao ? 'Conta atualizada' : 'Conta criada',
          message: contaEmEdicao
            ? 'As alterações foram salvas com sucesso.'
            : 'Sua conta foi adicionada com sucesso.',
        });

        this.modalAberto.set(false);
        this.contaEmEdicao.set(null);

        this.carregarContas();
      },

      error: ({ error }) => {
        const mensagem = this.errorService.getMessage(error ?? {});

        this.toastService.show({
          type: 'error',
          title: mensagem.title,
          message: mensagem.message,
        });
      },
    });
  }

  // ---------- Desativação ----------

  protected confirmarDesativacao(conta: ContaResponse): void {
    this.fecharMenu();
    this.contaParaDesativar.set(conta);
  }

  protected cancelarDesativacao(): void {
    if (this.desativando()) return;

    this.contaParaDesativar.set(null);
  }

  protected desativarConta(): void {
    const conta = this.contaParaDesativar();

    if (!conta) return;

    this.desativando.set(true);

    this.contaService
      .desativar(conta.id)
      .pipe(finalize(() => this.desativando.set(false)))
      .subscribe({
        next: () => {
          this.toastService.show({
            type: 'success',
            title: 'Conta desativada',
            message: `A conta "${conta.nome}" foi desativada.`,
          });

          this.contaParaDesativar.set(null);

          this.carregarContas();
        },

        error: ({ error }) => {
          const mensagem = this.errorService.getMessage(error ?? {});

          this.toastService.show({
            type: 'error',
            title: mensagem.title,
            message: mensagem.message,
          });
        },
      });
  }

  // ---------- Validação dinâmica ----------

  private atualizarValidadoresPorTipo(tipo: TipoConta): void {
    const instituicao = this.formulario.controls.instituicaoId;

    if (tipo === TipoConta.CARTEIRA) {
      instituicao.clearValidators();
    } else {
      instituicao.setValidators([Validators.required]);
    }

    instituicao.updateValueAndValidity({
      emitEvent: false,
    });
  }
}
