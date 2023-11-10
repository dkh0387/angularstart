import {Injectable} from '@angular/core';
import {RequestService} from "./extended/request.service";
import {HttpClient} from "@angular/common/http";
import {GlobalConstants} from "../shared/global-constants";

@Injectable({
  providedIn: 'root'
})
export class ProductService extends RequestService {

  constructor(httpClient: HttpClient) {
    super(httpClient);
  }

  addProduct(data: any) {
    return super.post("/" + GlobalConstants.productPath + "/add", data);
  }

  updateProduct(data: any) {
    return super.post("/" + GlobalConstants.productPath + "/update", data);
  }

  updateProductStatus(data: any) {
    return super.post("/" + GlobalConstants.productPath + "/updateStatus", data);
  }

  getProducts() {
    return super.get("/" + GlobalConstants.productPath + "/get");
  }

  deleteProduct(data: any) {
    return super.post("/" + GlobalConstants.productPath + `/delete/${data.id}`, {});
  }

  getProductsByCategory(data: any) {
    return super.get("/" + GlobalConstants.productPath + `/getByCategory/${data.categoryId}`);
  }

  getProductById(data: any) {
    return super.get("/" + GlobalConstants.productPath + `/getById/${data.id}`);
  }
}
