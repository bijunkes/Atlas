import { Component, DestroyRef, EventEmitter, inject, Input, Output, PLATFORM_ID, signal } from '@angular/core';

// Permite destruir automaticamente subscriptions quando o componente for destruído
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { CommonModule, isPlatformBrowser } from '@angular/common';
import { NavigationEnd, Router, RouterLink, RouterLinkActive } from '@angular/router';

// Operador do RxJS usado para filtrar apenas eventos específicos do Router
import { filter } from 'rxjs';

import {
  LucideAngularModule,
  LayoutDashboard,
  Wallet,
  ArrowLeftRight,
  Tags,
  Repeat,
  LogOut,
  Menu,
  X,
  ChevronLeft,
  ChevronRight,
  WalletCards,
  type LucideIconData
} from 'lucide-angular';

// Interface que define quais informações do usuário o Sidebar precisa receber
// Obs: O componente não busca o usuário sozinho, ele recebe através do componente pai
export interface SidebarUser {
  nome: string;
  email: string;
}

// Interface que representa cada item do menu lateral
interface NavItem {
  label: string;
  route: string;
  icon: LucideIconData;
  exact?: boolean;
}

// Chave usada para salvar no LocalStorage se o sidebar está recolhido, assim o estado permanece mesmo após atualizar a página
const SIDEBAR_COLLAPSED_KEY = 'sidebar-collapsed';

@Component({
  selector: 'app-sidebar',
  standalone: true,
  imports: [
    CommonModule,
    RouterLink,
    RouterLinkActive,
    LucideAngularModule
  ],
  templateUrl: './sidebar.html',
  styleUrl: './sidebar.css',
})
export class SidebarComponent {

  // Recebe os dados do usuário logado vindos do componente pai
  @Input() user: SidebarUser | null = null;

  // Evento enviado para o componente pai quando o usuário clicar em logout
  @Output() logout = new EventEmitter<void>();

  // Detecta se a aplicação está rodando no navegador
  private readonly platformId = inject(PLATFORM_ID);
  private readonly isBrowser = isPlatformBrowser(this.platformId);

  private readonly destroyRef = inject(DestroyRef);
  private readonly router = inject(Router);

  protected readonly icons = {
    LogOut,
    Menu,
    X,
    ChevronLeft,
    ChevronRight,
    WalletCards,
  };

  // Lista dos itens que aparecem no menu lateral
  protected readonly navItems: NavItem[] = [
    {
      label: 'Dashboard',
      route: '/dashboard',
      icon: LayoutDashboard,
      exact: true
    },
    {
      label: 'Contas',
      route: '/contas',
      icon: Wallet
    },
    {
      label: 'Transações',
      route: '/transacoes',
      icon: ArrowLeftRight
    },
    {
      label: 'Categorias',
      route: '/categorias',
      icon: Tags
    },
    {
      label: 'Recorrências',
      route: '/recorrencias',
      icon: Repeat
    }
  ];

  // Signal que controla se o menu mobile está aberto
  protected readonly isMobileOpen = signal(false);

  // Signal que controla se o sidebar está expandido ou recolhido
  protected readonly isCollapsed = signal(
    this.readCollapsedFromStorage()
  );

  constructor() {

    // Observa mudanças de rota
    this.router.events
      .pipe(

        // Considera somente eventos que indicam finalização da navegação
        filter((event) => event instanceof NavigationEnd),

        // Cancela a inscrição automaticamente quando o componente morrer
        takeUntilDestroyed(this.destroyRef)

      )
      .subscribe(() => this.closeMobileMenu());
  }

  // Abre ou fecha o menu lateral no mobile
  protected toggleMobileMenu(): void {
    this.isMobileOpen.update(
      (open) => !open
    );
  }

  // Fecha o menu mobile
  protected closeMobileMenu(): void {
    this.isMobileOpen.set(false);
  }

  // Alterna entre sidebar expandido e recolhido
  protected toggleCollapse(): void {
    const next = !this.isCollapsed();
    this.isCollapsed.set(next);
    this.writeCollapsedToStorage(next);
  }

  // Emite o evento de logout para o componente pai
  protected onLogoutClick(): void {
    this.logout.emit();
  }

  // Cria as iniciais do usuário para mostrar no avatar
  protected get initials(): string {
    if (!this.user?.nome) return '';

    return this.user.nome
      .trim()
      .split(/\s+/)
      .slice(0, 2)
      .map((part) => part[0]?.toUpperCase())
      .join('');
  }

  // Busca no navegador se o usuário já deixou o sidebar recolhido
  private readCollapsedFromStorage(): boolean {
    if (!this.isBrowser) return false;
    return localStorage.getItem(SIDEBAR_COLLAPSED_KEY) === 'true';
  }

  private writeCollapsedToStorage(value: boolean): void {
    if (!this.isBrowser) return;

    localStorage.setItem(
      SIDEBAR_COLLAPSED_KEY,
      String(value)
    );
  }

}