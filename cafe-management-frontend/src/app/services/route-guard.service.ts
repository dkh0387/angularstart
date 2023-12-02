import {Injectable} from '@angular/core';
import {AuthService} from "./auth.service";
import {ActivatedRouteSnapshot, Router} from "@angular/router";
import {SnackbarService} from "./snackbar.service";
import jwt_decode from 'jwt-decode';
import {PayPalService} from "./paypal.service";
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

  constructor(public authService: AuthService,
              public router: Router,
              private snackbarService: SnackbarService,
              private payPalService: PayPalService) {
  }

  /**
   * NOTE: at the moment, there is no role concept required!
   * We only prove the existence of a transaction id after user PayPal payment is completed.
   * @param activatedRouteSnapshot
   */
  canActivate(activatedRouteSnapshot: ActivatedRouteSnapshot) {
    const resolvedUrl = this.getResolvedUrl(activatedRouteSnapshot);

    if (resolvedUrl.includes(GlobalConstants.paypalConfirmationPath)) {
      const transactionId = activatedRouteSnapshot.paramMap.get(GlobalConstants.transactionIdKey);

      if (transactionId != null) {
        //const regexp = new RegExp(GlobalConstants.transactionIdRegex);
        //return regexp.test(transactionId);
        return transactionId === this.transactionId;
      }
    }
    return true;
  }

  decodeToken() {
    const token: any = this.authService.getToken();
    return jwt_decode(token);
  }

  private getResolvedUrl(activatedRouteSnapshot: ActivatedRouteSnapshot): string {
    return activatedRouteSnapshot.pathFromRoot
      .map(v => v.url.map(segment => segment.toString()).join('/'))
      .join('/');
  }

  get transactionId(): string {
    return this.payPalService.transactionId;
  }

  set transactionId(value: string) {
    this.payPalService.transactionId = value;
  }
}
