import { 
  provideHttpClient, 
  withInterceptors,
  withXsrfConfiguration
} from '@angular/common/http';

import { authInterceptor } from './core/interceptors/auth.interceptor';
import { provideRouter, withInMemoryScrolling } from '@angular/router';
import { ApplicationConfig } from '@angular/core';
import { routes } from './app.routes';
import { errorInterceptor } from './core/interceptors/error.interceptor';


export const appConfig: ApplicationConfig = {

  providers: [

    provideRouter(
      routes,
      withInMemoryScrolling({
        scrollPositionRestoration: 'enabled'
      })
    ),

    provideHttpClient(

      withXsrfConfiguration({
        cookieName: 'XSRF-TOKEN',
        headerName: 'X-XSRF-TOKEN'
      }),

      withInterceptors([
        authInterceptor,
        errorInterceptor
      ])
    )

  ]

};