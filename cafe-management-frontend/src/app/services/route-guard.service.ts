import {Injectable} from '@angular/core';
import {AuthService} from "./auth.service";
import {ActivatedRouteSnapshot, Router} from "@angular/router";
import {SnackbarService} from "./snackbar.service";
import jwt_decode from 'jwt-decode';
import {GlobalConstants} from "../shared/global-constants";

/**
 * CanActivate() handler used to determine if the current user is allowed to activate the component,
 * where this handler is injected to check (see [app-routing-module.ts] for usage).
 * By default, any user can activate.
 */
@Injectable({
  providedIn: 'root'
})
export class RouteGuardService {

  constructor(public authService: AuthService, public router: Router, private snackbarService: SnackbarService) {
  }

  /**
   * NOTE: at the moment, there is no role concept required!
   * @param activatedRouteSnapshot
   */
  canActivate(activatedRouteSnapshot: ActivatedRouteSnapshot) {
    return true;

  }

  decodeToken() {
    const token: any = this.authService.getToken();
    return jwt_decode(token);
  }
}
