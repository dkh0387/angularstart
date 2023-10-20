import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {environment} from "../../environments/environment";

/**
 * @TODO: testing!
 */
@Injectable({
  providedIn: 'root'
})
export class DashboardService {

  constructor(private httpClient: HttpClient) {
  }

  getDetails() {
    return this.httpClient.get(environment.apiUrl + "/dashboard/details")
  }
}
