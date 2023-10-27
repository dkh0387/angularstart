import {Injectable} from '@angular/core';
import {environment} from "../../environments/environment";
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {GlobalConstants} from "../shared/global-constants";

@Injectable({
  providedIn: 'root'
})
export class CategoryService {

  private categoryPath = GlobalConstants.categoryPath;
  url = environment.apiUrl;

  constructor(private httpClient: HttpClient) {
  }

  add(data: any) {
    return this.post("/" + this.categoryPath + "/add", data);
  }

  update(data: any) {
    return this.post("/" + this.categoryPath + "/update", data);
  }

  getCategories() {
    return this.get("/" + this.categoryPath + "/get");
  }

  private post(endpointUrl: string, data: any) {
    return this.httpClient.post(this.url + endpointUrl, data, {
      headers: new HttpHeaders().set('content-Type', "application/json")
    });
  }

  private get(endpointUrl: string) {
    return this.httpClient.get(this.url + endpointUrl);
  }
}
