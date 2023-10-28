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
   * @param activatedRouteSnapshot
   */
  canActivate(activatedRouteSnapshot: ActivatedRouteSnapshot) {
    // variables declared with let have a block-scope
    let expectedRoleArray = activatedRouteSnapshot.data.expectedRole;
    let tokenPayload: any;

    // decode a JWT using jwt_decode
    try {
      tokenPayload = this.decodeToken();
    } catch (error) {
      localStorage.clear();
      this.router.navigate(['/']);
      throw new Error('Invalid token specified')
    }

    /* if user or admin, but unauthenticated or not allowed, navigate to the dashboard;
    * if either user nor admin navigates to homepage
    * is authenticated, enable activation
     */
    if (tokenPayload.role == GlobalConstants.roleUser || tokenPayload.role == GlobalConstants.roleAdmin) {
      if (this.authService.isAuthenticated() && expectedRoleArray.includes(tokenPayload.role )) {
        return true;
      }
      this.snackbarService.openSnackBar(GlobalConstants.unauthorized, GlobalConstants.error);
      // navigate to dashboard, since only user or admin are allowed
      this.router.navigate(['/' + GlobalConstants.homePath + '/' + GlobalConstants.dashboardPath]);
      return false;
    }
    this.router.navigate(['/']);
    localStorage.clear();
    return false;

  }

  decodeToken() {
    const token: any = this.authService.getToken();
    return jwt_decode(token);
  }
}
