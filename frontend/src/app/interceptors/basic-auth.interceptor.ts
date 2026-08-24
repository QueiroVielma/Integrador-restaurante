import { HttpInterceptorFn } from '@angular/common/http';
import { environment } from '../../environments/environment';

export const basicAuthInterceptor: HttpInterceptorFn = (req, next) => {
  const token = btoa(`${environment.basicAuthUser}:${environment.basicAuthPassword}`);

  return next(
    req.clone({
      setHeaders: {
        Authorization: `Basic ${token}`
      }
    })
  );
};
