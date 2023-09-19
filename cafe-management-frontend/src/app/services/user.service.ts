import {Injectable} from '@angular/core';
import {environment} from "../../environments/environment";
import {HttpClient, HttpHeaders} from "@angular/common/http";

@Injectable({
  providedIn: 'root'
})
export class UserService {

  url = environment.apiUrl

  constructor(private httpClient: HttpClient) {
  }

  /**
   * NOTE: this one calls the according endpoint in backend.
   * @param data
   */
  signUp(data: any) {
    return this.httpClient.post(this.url + "/user/signup", data, {
      headers: new HttpHeaders().set('Content-type', 'application/json')
    })
  }
}
