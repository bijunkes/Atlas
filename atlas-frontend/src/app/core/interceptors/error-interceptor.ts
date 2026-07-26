import { HttpErrorResponse, HttpInterceptorFn } from '@angular/common/http';
import { catchError, throwError } from 'rxjs';

export const errorInterceptor: HttpInterceptorFn = (req, next) => {

    return next(req).pipe(

        catchError((error: HttpErrorResponse) => {

            if(error.error){
                console.log(
                    'Erro da API:',
                    error.error
                );
            }

            return throwError(() => error);
        })
    );
};