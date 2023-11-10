import {Component, EventEmitter, Inject, OnInit} from '@angular/core';
import {ResponseHadler} from "../../../extended/response-handler";
import {SubmitHandler} from "../../../interfaces/submit-handler";
import {RestSubscriber} from "../../../interfaces/rest-subscriber";
import {Observable} from "rxjs";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {GlobalConstants} from "../../../shared/global-constants";
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";
import {ProductService} from "../../../services/product.service";
import {CategoryService} from "../../../services/category.service";
import {SnackbarService} from "../../../services/snackbar.service";
import {BillService} from "../../../services/bill.service";

@Component({
  selector: 'app-order',
  templateUrl: './order.component.html',
  styleUrls: ['./order.component.scss']
})
export class OrderComponent extends ResponseHadler implements OnInit, SubmitHandler, RestSubscriber {

  onAddProduct = new EventEmitter();
  onEditProduct = new EventEmitter();
  orderForm: any = FormGroup;
  dialogAction: any = GlobalConstants.dialogActionAdd;
  action: any = GlobalConstants.dialogActionAdd;
  categories: any = [];
  products: any = [];
  price: any;
  total: number = 0;

  constructor(@Inject(MAT_DIALOG_DATA) public dialogData: any,
              private formBuilder: FormBuilder,
              private productService: ProductService,
              private categoryService: CategoryService,
              private billService: BillService,
              public dialogRef: MatDialogRef<OrderComponent>,
              private snackBarService: SnackbarService) {
    super();
  }

  ngOnInit(): void {
    this.orderForm = this.formBuilder.group({
      name: [null, [Validators.required, Validators.pattern(GlobalConstants.nameRegex)]],
      email: [null, [Validators.required, Validators.pattern(GlobalConstants.emailRegex)]],
      contactNumber: [null, [Validators.required, Validators.pattern(GlobalConstants.contactNumberRegex)]],
      paymentMethod: [null, [Validators.required]],
      product: [null, [Validators.required]],
      category: [null, [Validators.required]],
      quantity: [null, [Validators.pattern(GlobalConstants.numberRegex)]],
      price: [null, [Validators.pattern(GlobalConstants.priceRegex)]],
      total: [0, [Validators.pattern(GlobalConstants.priceRegex)]]
    });

    if (this.dialogData.action === GlobalConstants.dialogActionEdit) {
      this.dialogAction = GlobalConstants.dialogActionEdit;
      this.action = "update";
      this.orderForm.patchValue(this.dialogData.data);
    }
    this.getCategories();
  }

  handleSubmit(): void {
  }

  subscribe(observable: Observable<Object>): void {
  }

  private getCategories() {
    return this.subscribeForCategories(this.categoryService.getFilteredCategories());
  }

  private subscribeForCategories(observable: Observable<Object>): void {
    observable.subscribe((response: any) => {
      this.categories = response;
    }, (error: any) => {
      super.logAndShowError(error, this.snackBarService);
    })
  }
}
