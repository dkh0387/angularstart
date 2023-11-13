import {Injectable} from '@angular/core';
import {ProductService} from "./product.service";
import {CategoryService} from "./category.service";
import {BillService} from "./bill.service";

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


  getProductsByCategory(categoryId: any) {
    return this.productService.getProductsByCategory(categoryId);
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
