import {Injectable} from "@angular/core";
import {environment} from "../../../environments/environment";
import {HttpClient, HttpHeaders} from "@angular/common/http";

@Injectable({providedIn: "root"})
export class RequestService {

  url = environment.apiUrl;

  constructor(protected httpClient: HttpClient) {
  }

  protected post(endpointUrl: string, data: any) {
    return this.httpClient.post(this.url + endpointUrl, data, {
      headers: new HttpHeaders().set("content-Type", "application/json")
    });
  }

  protected postForFile(endpointUrl: string, data: any) {
    return this.httpClient.post(this.url + endpointUrl, data, {responseType: "blob"});
  }

  protected get(endpointUrl: string) {
    return this.httpClient.get(this.url + endpointUrl);
  }

}
