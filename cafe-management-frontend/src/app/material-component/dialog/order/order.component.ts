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
              public dialogRef: MatDialogRef<OrderComponent>) {
    super();
  }

  ngOnInit(): void {

  }

  handleSubmit(): void {
  }

  subscribe(observable: Observable<Object>): void {
  }
}
