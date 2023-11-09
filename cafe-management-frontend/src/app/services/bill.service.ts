import {Injectable} from '@angular/core';
import {RequestService} from "./extended/request.service";
import {HttpClient} from "@angular/common/http";
import {GlobalConstants} from "../shared/global-constants";
import {Observable} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class BillService extends RequestService {

  constructor(httpClient: HttpClient) {
    super(httpClient);
  }

  generateBill(data: any) {
    return super.post("/" + GlobalConstants.billPath + "/generate", data);
  }

  getBillDocument(data: any) {
    return super.postForFile("/" + GlobalConstants.billPath + "/getBillDocument", data);
  }

  getBills() {
    return super.get("/" + GlobalConstants.billPath + "/get");
  }


}
