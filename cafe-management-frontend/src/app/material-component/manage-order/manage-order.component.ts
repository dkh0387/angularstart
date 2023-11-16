import {Component} from '@angular/core';
import {ItemManager} from "../../extended/item-manager";
import {NgxUiLoaderService} from "ngx-ui-loader";
import {MatDialog} from "@angular/material/dialog";
import {SnackbarService} from "../../services/snackbar.service";
import {Router} from "@angular/router";
import {OrderService} from "../../services/order.service";
import {Observable} from "rxjs";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {GlobalConstants} from "../../shared/global-constants";
import {SubmitHandler} from "../../interfaces/submit-handler";

@Component({
  selector: 'app-manage-order',
  templateUrl: './manage-order.component.html',
  styleUrls: ['./manage-order.component.scss']
})
export class ManageOrderComponent extends ItemManager implements SubmitHandler {

  displayedColumns: string[] = ["name", "category", "price", "quantity", "total", "edit"];
  manageOrderForm: any = FormGroup;
  categories: any = [];
  products: any = [];
  price: any;
  total: number = 0;
  productNameById: any;
  categoryNameById: any;

  constructor(private orderService: OrderService,
              private formBuilder: FormBuilder,
              ngxService: NgxUiLoaderService,
              dialog: MatDialog,
              snackbarService: SnackbarService,
              router: Router) {
    super(ngxService, dialog, snackbarService, router);
  }

  ngOnInit(): void {
    super.ngOnInit();
    this.manageOrderForm = this.formBuilder.group({
      name: [null, [Validators.required, Validators.pattern(GlobalConstants.nameRegex)]],
      email: [null, [Validators.required, Validators.pattern(GlobalConstants.emailRegex)]],
      contactNumber: [null, [Validators.required, Validators.pattern(GlobalConstants.contactNumberRegex)]],
      paymentMethod: [null, [Validators.required]],
      product: [null, [Validators.required]],
      category: [null, [Validators.required]],
      quantity: [null, [Validators.pattern(GlobalConstants.quantityRegex)]],
      price: [null, [Validators.pattern(GlobalConstants.priceRegex)]],
      total: [0, [Validators.pattern(GlobalConstants.priceRegex)]]
    });
  }

  loadData() {
    this.dataSource = [];
    this.getFilteredCategories();
    this.ngxService.stop();
  }

  getProductDetails(productId: any) {
    this.subscribeForProductDetails(this.orderService.getProductDetails(productId));
    this.loadObjectsById(productId);
  }

  getProductsByCategory(categoryId: any) {
    this.subscribeForProducts(this.orderService.getProductsByCategory(categoryId));
  }

  /**
   * Get only categories with active products.
   */
  getFilteredCategories() {
    this.subscribeForCategories(this.orderService.getFilteredCategories());
  }

  /**
   * Remove a product from the actual order.
   * @param data
   * @param element
   */
  handleDropAction(data: any, element: any) {
    this.total -= element.total;
    this.dataSource.splice(data, 1);
    super.updateDataSource();
  }

  /**
   * Place order and generate/download a bill.
   */
  handleSubmit(): void {
    const formData = this.manageOrderForm.value;
    const data = {
      name: formData.name,
      email: formData.email,
      contactNumber: formData.contactNumber,
      paymentMethod: formData.paymentMethod,
      total: this.total.toString(),
      productDetails: JSON.stringify(this.dataSource)
    };
    this.ngxService.start();
    this.subscribeForBill(this.orderService.generateBill(data));
  }

  /**
   * Validate the button ability to add a new product to the actual order.
   */
  validateProductAdd() {
    const total = this.manageOrderForm.controls["total"].value;
    return total === null || total === 0;
  }

  /**
   * Controls the button ability to place an order.
   */
  validateSubmit() {
    const name = this.manageOrderForm.controls["name"].value;
    const email = this.manageOrderForm.controls["email"].value;
    const contactNumber = this.manageOrderForm.controls["contactNumber"].value;
    const paymentMethod = this.manageOrderForm.controls["paymentMethod"].value;

    return this.total === 0 || name === null || email === null || contactNumber === null || paymentMethod === null;
  }

  /**
   * Adding a new product to a table datasource overview.
   */
  add() {
    const formData = this.manageOrderForm.value;
    const productName = this.dataSource.find((e: { id: number }) => e.id === formData.product);

    if (productName === undefined) {
      this.total += formData.total;
      this.dataSource.push({
        id: formData.product,
        name: this.productNameById,
        category: this.categoryNameById,
        quantity: formData.quantity,
        price: formData.price,
        total: formData.total
      });
      super.updateDataSource();
      this.snackbarService.openSnackBar(GlobalConstants.productAdded, GlobalConstants.success);
    } else {
      this.snackbarService.openSnackBar(GlobalConstants.productExistsError, GlobalConstants.error);
    }
  }

  setTotal(data: any) {
    this.manageOrderForm.controls["total"]
      .setValue(this.manageOrderForm.controls["price"].value * this.manageOrderForm.controls["quantity"].value);

  }

  reset() {
    super.reset();
    this.manageOrderForm.reset();
    this.total = 0;
  }

  private subscribeForCategories(observable: Observable<Object>): void {
    observable.subscribe((response: any) => {
      this.categories = response;
    }, (error: any) => {
      super.logAndShowError(error, this.snackbarService);
    })
  }

  private subscribeForProducts(observable: Observable<Object>): void {
    observable.subscribe((response: any) => {
      this.products = response;
      this.manageOrderForm.controls["price"].setValue("");
      this.manageOrderForm.controls["quantity"].setValue("");
      this.manageOrderForm.controls["total"].setValue(0);
    }, (error: any) => {
      super.logAndShowError(error, this.snackbarService);
    })
  }

  private subscribeForProductDetails(observable: Observable<Object>): void {
    observable.subscribe((response: any) => {
      this.price = response.price;
      this.manageOrderForm.controls["price"].setValue(response.price);
      this.manageOrderForm.controls["quantity"].setValue("1");
      this.manageOrderForm.controls["total"].setValue(this.price * 1);
    }, (error: any) => {
      super.logAndShowError(error, this.snackbarService);
    })
  }

  private subscribeForBill(observable: Observable<Object>): void {
    observable.subscribe((response: any) => {
      super.subscribeForBillDownload(this.orderService.getBillDocument({uuid: response}), response);
      this.reset();
      this.ngxService.stop();
    }, (error: any) => {
      super.logAndShowError(error, this.snackbarService);
    });
  }

  private loadObjectsById(productId: any) {
    this.subscribeForObjectsById(this.orderService.getProductById(productId));
  }

  private subscribeForObjectsById(observable: Observable<Object>): void {
    observable.subscribe((response: any) => {
      this.productNameById = response.name;
      this.categoryNameById = response.categoryName;
    }, (error: any) => {
      super.logAndShowError(error, this.snackbarService);
    })
  }
}
