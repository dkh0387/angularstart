import {Injectable} from '@angular/core';
import {
  HttpRequest,
  HttpHandler,
  HttpEvent,
  HttpInterceptor, HttpErrorResponse
} from '@angular/common/http';
import {Observable, throwError} from 'rxjs';
import {Router} from "@angular/router";
import {AuthService} from "./auth.service";
import {catchError} from "rxjs/operators";
import {PayPalService} from "./paypal.service";

/**
 * @TODO: testing!
 * Interceptor to handle response from authentication API.
 */
@Injectable()
export class TokenInterceptor implements HttpInterceptor {

  constructor(private router: Router, private authService: AuthService) {
  }

  /**
   * After we got the JWT we clone an HTTP request with Bearer Authentication header to reach secured components.
   * @param request
   * @param next
   */
  intercept(request: HttpRequest<unknown>, next: HttpHandler): Observable<HttpEvent<unknown>> {
    const token = this.authService.getToken();

    if (token != null) {
      request = request.clone({setHeaders: {Authorization: `Bearer ${token}`}});
    }
    return next.handle(request).pipe(
      catchError((error) => {
          if (error instanceof HttpErrorResponse) {
            console.log(error.url);

            if (error.status == 401 || error.status == 403) {
              if (error.url === '/') {

              } else {
                localStorage.clear();
                this.router.navigate(['/']);
              }
            }
          }
          return throwError(error);
        }
      )
    );
  }
}
