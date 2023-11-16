import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {RequestService} from "./extended/request.service";
import {GlobalConstants} from "../shared/global-constants";

@Injectable({
  providedIn: 'root'
})
export class UserService extends RequestService {

  constructor(httpClient: HttpClient) {
    super(httpClient);
  }

  /**
   * NOTE: this one calls the according endpoint in backend.
   * @param data
   */
  signUp(data: any) {
    return super.post(+"/" + GlobalConstants.userPath + "/" + "signup", data);
  }

  forgotPassword(data: any) {
    return super.post("/" + GlobalConstants.userPath + "/" + "forgotPassword", data);
  }

  login(data: any) {
    return super.post("/" + GlobalConstants.userPath + "/" + "login", data);
  }

  checkToken() {
    return super.get("/" + GlobalConstants.userPath + "/" + "checkToken");
  }

  changePassword(data: any) {
    return super.post("/" + GlobalConstants.userPath + "/" + "changePassword", data);
  }

  getUsers() {
    return super.get("/" + GlobalConstants.userPath + "/" + "get");
  }

  updateUser(data: any) {
    return super.post("/" + GlobalConstants.userPath + "/" + "update", data);
  }
}
