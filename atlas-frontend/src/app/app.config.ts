import { ApplicationConfig, inject } from '@angular/core';
import { provideRouter, withInMemoryScrolling } from '@angular/router';
import { provideHttpClient, withInterceptors } from '@angular/common/http';

import { routes } from './app.routes';

import { authInterceptor } from './core/interceptors/auth.interceptor';
import { csrfInterceptor } from './core/interceptors/csrf.interceptor';
import { errorInterceptor } from './core/interceptors/error.interceptor';

import { provideAppInitializer } from '@angular/core';
import { CsrfService } from './core/services/csrf.service';

export const appConfig: ApplicationConfig = {
  providers: [

    provideRouter(
      routes,
      withInMemoryScrolling({
        scrollPositionRestoration: 'enabled',
      }),
    ),

    provideHttpClient(
      withInterceptors([
        authInterceptor,
        csrfInterceptor,
        errorInterceptor,
      ]),
    ),

    provideAppInitializer(() => {
      const csrfService = inject(CsrfService);
      return csrfService.inicializar();
    }),
  ],
};