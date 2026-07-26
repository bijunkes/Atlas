import { Routes } from '@angular/router';

import { LandingComponent } from './features/landing/landing';
import { LoginComponent } from './features/auth/login/login';
import { RegisterComponent } from './features/auth/register/register';
import { DashboardComponent } from './features/dashboard/dashboard';
import { authGuard } from './core/guards/auth-guard';

export const routes: Routes = [
    {
        path: 'lp',
        loadComponent: () =>
        import('./features/landing/landing')
            .then(m => m.LandingComponent),
    },

    {
        path: 'login',
        loadComponent: () =>
        import('./features/auth/login/login')
            .then(m => m.LoginComponent)
    },

    {
        path: 'register',
        loadComponent: () =>
        import('./features/auth/register/register')
            .then(m => m.RegisterComponent)
    },

    {
        path: 'dashboard',
        loadComponent: () =>
        import('./features/dashboard/dashboard')
            .then(m => m.DashboardComponent),

        canActivate: [authGuard]
    },

    {
        path: '',
        redirectTo: 'lp',
        pathMatch: 'full'
    },

    {
        path: '**',
        redirectTo: 'lp'
    }
];
