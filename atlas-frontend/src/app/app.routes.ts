import { Routes } from '@angular/router';

import { authGuard } from './core/guards/auth-guard';

export const routes: Routes = [
  {
    path: 'lp',
    loadComponent: () => import('./features/landing/landing').then((m) => m.LandingComponent),
  },

  {
    path: 'login',
    loadComponent: () => import('./features/auth/login/login').then((m) => m.LoginComponent),
  },

  {
    path: 'register',
    loadComponent: () =>
      import('./features/auth/register/register').then((m) => m.RegisterComponent),
  },

  {
    path: '',
    loadComponent: () =>
      import('./features/authenticated/authenticated').then((m) => m.AuthenticatedComponent),

    canActivate: [authGuard],

    children: [
      {
        path: 'dashboard',
        loadComponent: () =>
          import('./features/dashboard/dashboard').then((m) => m.DashboardComponent),
      },

      {
        path: 'contas',
        loadComponent: () => import('./features/contas/contas').then((m) => m.ContasComponent),
      },

      {
        path: 'transacoes',
        loadComponent: () =>
          import('./features/transacoes/transacoes').then((m) => m.TransacoesComponent),
      },

      {
        path: 'categorias',
        loadComponent: () =>
          import('./features/categorias/categorias').then((m) => m.CategoriasComponent),
      },

      {
        path: 'recorrencias',
        loadComponent: () =>
          import('./features/recorrencias/recorrencias').then((m) => m.RecorrenciasComponent),
      },

      {
        path: 'perfil',
        loadComponent: () => import('./features/perfil/perfil').then((m) => m.PerfilComponent),

        canActivate: [authGuard],
      },
    ],
  },

  {
    path: '',
    redirectTo: 'lp',
    pathMatch: 'full',
  },

  {
    path: '**',
    redirectTo: 'lp',
  },
];
