import {Injectable} from '@angular/core';
import {AuthService} from "./auth.service";
import {ActivatedRouteSnapshot, Router} from "@angular/router";
import {SnackbarService} from "./snackbar.service";
import jwt_decode from 'jwt-decode';
import {GlobalConstants} from "../shared/global-constants";

/**
 * @TODO: testing!
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
    const token: any = this.authService.getToken();
    let tokenPayload: any;

    // decode a JWT using jwt_decode
    try {
      tokenPayload = jwt_decode(token);
    } catch (error) {
      localStorage.clear();
      this.router.navigate(['/']);
    }
    let expectedRole = '';

    /* Find and set the role
     * NOTE: JWTService in backend generates JWTs using authorities (see UserService.logIn()),
     * so the role in response is like ROLE_<ROLENAME>!
     */
    for (let i = 0; i < expectedRoleArray.length; i++) {
      if (expectedRoleArray[i] == tokenPayload.role) {
        expectedRole = tokenPayload.role;
      }
    }

    // if user or admin, but unauthenticated, navigate to the dashboard, if authenticated enable activation
    if (expectedRole == GlobalConstants.roleUser || expectedRole == GlobalConstants.roleAdmin) {
      if (this.authService.isAuthenticated() && expectedRole == tokenPayload.role) {
        return true;
      }
      this.snackbarService.openSnackBar(GlobalConstants.unauthorized, GlobalConstants.error);
      // navigate to dashboard, since only user or admin are allowed
      this.router.navigate(['/cafe/dashboard']);
      return false;
    }
    this.router.navigate(['/']);
    localStorage.clear();
    return false;
  }
}
