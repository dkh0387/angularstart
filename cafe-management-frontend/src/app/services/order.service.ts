import {Injectable} from '@angular/core';
import {ProductService} from "./product.service";
import {CategoryService} from "./category.service";
import {BillService} from "./bill.service";
import {SnackbarService} from "./snackbar.service";

@Injectable({
  providedIn: 'root'
})
export class OrderService {

  constructor(private productService: ProductService,
              private categoryService: CategoryService,
              private billService: BillService) {
  }

  getFilteredCategories() {
    return this.categoryService.getFilteredCategories();
  }


  getProductsByCategory(data: any) {
    return this.productService.getProductsByCategory(data);
  }

  getProductDetails(data: any) {
    return this.productService.getProductById(data);
  }

  generateBill(data: any) {
    return this.billService.generateBill(data);
  }

  getBillDocument(data: any) {
    return this.billService.getBillDocument(data);
  }
}
