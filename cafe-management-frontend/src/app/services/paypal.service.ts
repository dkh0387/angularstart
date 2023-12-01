import {Injectable} from '@angular/core';
import {RequestService} from "./extended/request.service";
import {HttpClient, HttpHeaders, HttpParams} from "@angular/common/http";
import {GlobalConstants} from "../shared/global-constants";

@Injectable({
  providedIn: 'root'
})
export class PayPalService extends RequestService {

  accessResponse: any;
  private _transactionId = "";

  constructor(httpClient: HttpClient) {
    super(httpClient);
  }

  getAccessToken() {
    this.subscribe();
    return {token_type: this.accessResponse.token_type, access_token: this.accessResponse.access_token};
  }

  postForAccessToken() {
    const credentials = btoa(`${GlobalConstants.payPalAuthClientID}:${GlobalConstants.payPalAuthClientSecret}`);
    const params = new HttpParams().set("grant_type", "client_credentials");
    return this.httpClient.post(GlobalConstants.payPalAuthTokenAPI, params, {
      headers: new HttpHeaders({Authorization: `Basic ${credentials}`}).set("content-Type", "application/x-www-form-urlencoded")
    });
  }

  get transactionId(): string {
    return this._transactionId;
  }

  set transactionId(value: string) {
    this._transactionId = value;
  }

  private subscribe(): void {
    this.postForAccessToken().subscribe((response: any) => {
      this.accessResponse = response;
    }, (error: any) => {
      console.log(error);
    });
  }

}
