import {Injectable} from '@angular/core';
import {Router} from "@angular/router";
import {getToken} from "codelyzer/angular/styles/cssLexer";

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  constructor(private router: Router) {
  }

  /**
   * Just checking, whether a JWT exists.
   */
  isAuthenticated() {
    const token = this.getToken()
    // if no token exists, navigate to
    if (!token) {
      this.router.navigate(['/']);
      return false;
    }
    return true;
  }

  getToken() {
    return localStorage.getItem('token')
  }
}
