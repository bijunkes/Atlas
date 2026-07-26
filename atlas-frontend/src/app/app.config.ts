import { provideHttpClient, withInterceptors } from '@angular/common/http';
import { authInterceptor } from './core/interceptors/auth-interceptor';
import { provideRouter, withInMemoryScrolling  } from '@angular/router';
import { ApplicationConfig } from '@angular/core';
import { routes } from './app.routes';
import { errorInterceptor } from './core/interceptors/error-interceptor';


export const appConfig: ApplicationConfig = {

  providers: [

    provideRouter(routes),

    provideHttpClient(
      withInterceptors([
        authInterceptor,
        errorInterceptor
      ])
    ),

    provideRouter(routes, withInMemoryScrolling({
      scrollPositionRestoration: 'enabled'
    }))

  ]

};