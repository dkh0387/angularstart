import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {RequestService} from "./request.service";
import {GlobalConstants} from "../shared/global-constants";

@Injectable({providedIn: 'root'})
export class DashboardService extends RequestService {

  dashboardDetailsPath = "/" + GlobalConstants.dashboardPath + "/" + GlobalConstants.dashboardDetailsPath;

  constructor(httpClient: HttpClient) {
    super(httpClient);
  }

  getDetails() {
    return super.get(this.dashboardDetailsPath);
  }
}
