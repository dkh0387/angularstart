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
  private _documentName = "";
  private _documentPrice = "";

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

  private subscribe(): void {
    this.postForAccessToken().subscribe((response: any) => {
      this.accessResponse = response;
    }, (error: any) => {
      console.log(error);
    });
  }

  get transactionId(): string {
    return this._transactionId;
  }

  set transactionId(value: string) {
    this._transactionId = value;
  }

  get documentName(): string {
    return this._documentName;
  }

  set documentName(value: string) {
    this._documentName = value;
  }

  get documentPrice(): string {
    return this._documentPrice;
  }

  set documentPrice(value: string) {
    this._documentPrice = value;
  }

}
