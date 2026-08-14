import { HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';
import { switchMap } from 'rxjs';

import { CsrfService } from '../services/csrf.service';

export const csrfInterceptor: HttpInterceptorFn = (req, next) => {
  const csrfService = inject(CsrfService);

  const metodo = req.method.toUpperCase();

  if (['GET', 'HEAD', 'OPTIONS'].includes(metodo)) {
    return next(
      req.clone({
        withCredentials: true,
      }),
    );
  }
  
  if (req.url.includes('/csrf')) {
    return next(
      req.clone({
        withCredentials: true,
      }),
    );
  }

  if (
    req.url.includes('/auth/login') ||
    req.url.includes('/auth/register') ||
    req.url.includes('/auth/logout') ||
    req.url.includes('/auth/recuperar-senha') ||
    req.url.includes('/auth/resetar-senha') ||
    req.url.includes('/oauth2/') ||
    req.url.includes('/login/oauth2/')
  ) {
    return next(
      req.clone({
        withCredentials: true,
      }),
    );
  }

  return csrfService.obterToken().pipe(
    switchMap((token) => {
      return next(
        req.clone({
          withCredentials: true,
          setHeaders: {
            'X-XSRF-TOKEN': token,
          },
        }),
      );
    }),
  );
};