import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {GlobalConstants} from "../shared/global-constants";
import {RequestService} from "./request.service";

@Injectable({providedIn: 'root'})
export class CategoryService extends RequestService {

  constructor(httpClient: HttpClient) {
    super(httpClient);
  }

  add(data: any) {
    return super.post("/" + GlobalConstants.categoryPath + "/add", data);
  }

  update(data: any) {
    return super.post("/" + GlobalConstants.categoryPath + "/update", data);
  }

  getCategories() {
    return super.get("/" + GlobalConstants.categoryPath + "/get");
  }

}
