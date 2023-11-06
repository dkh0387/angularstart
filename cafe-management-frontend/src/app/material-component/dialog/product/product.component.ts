import {Component, EventEmitter, Inject, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {GlobalConstants} from "../../../shared/global-constants";
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";
import {SnackbarService} from "../../../services/snackbar.service";
import {ResponseHadler} from "../../../extended/response-handler";
import {SubmitHandler} from "../../../interfaces/submit-handler";
import {RestSubscriber} from "../../../interfaces/rest-subscriber";
import {observable, Observable} from "rxjs";
import {ProductService} from "../../../services/product.service";
import {CategoryService} from "../../../services/category.service";

@Component({
  selector: 'app-product',
  templateUrl: './product.component.html',
  styleUrls: ['./product.component.scss']
})
export class ProductComponent extends ResponseHadler implements OnInit, SubmitHandler, RestSubscriber {

  onAddProduct = new EventEmitter();
  onEditProduct = new EventEmitter();
  productForm: any = FormGroup;
  dialogAction: any = GlobalConstants.dialogActionAdd;
  action: any = GlobalConstants.dialogActionAdd;
  categories: any = [];
  defaultStatus = GlobalConstants.defaultProductStatus;

  constructor(@Inject(MAT_DIALOG_DATA) public dialogData: any,
              private formBuilder: FormBuilder,
              private productService: ProductService,
              private categoryService: CategoryService,
              public dialogRef: MatDialogRef<ProductComponent>,
              private snackBarService: SnackbarService) {
    super();
  }

  ngOnInit(): void {
    this.productForm = this.formBuilder.group({
      name: [null, [Validators.required, Validators.pattern(GlobalConstants.nameRegex)]],
      description: [null, [Validators.required]],
      price: [null, [Validators.pattern(GlobalConstants.priceRegex)]],
      categoryId: [null, [Validators.required]],
      status: [null, [Validators.pattern(GlobalConstants.statusRegex)]]
    });

    if (this.dialogData.action === GlobalConstants.dialogActionEdit) {
      this.dialogAction = GlobalConstants.dialogActionEdit;
      this.action = "update";
      this.productForm.patchValue(this.dialogData.data);
    }
    this.getCategories();
  }

  handleSubmit(): void {
    if (this.dialogAction === GlobalConstants.dialogActionEdit) {
      this.edit();
    } else {
      this.add();
    }
  }

  subscribe(observable: Observable<Object>): void {
    observable.subscribe((response: any) => {
      this.dialogRef.close();

      if (this.dialogAction === GlobalConstants.dialogActionAdd) {
        this.onAddProduct.emit();
      } else {
        this.onEditProduct.emit();
      }
      this.responseMessage = response;
      this.snackBarService.openSnackBar(this.responseMessage, GlobalConstants.success);
    }, (error) => {
      this.dialogRef.close();
      console.log(error);
      super.buildResponseMessageFrom(error);
      this.snackBarService.openSnackBar(this.responseMessage, GlobalConstants.error);
    })
  }

  subscribeForCategories(observable: Observable<Object>): void {
    observable.subscribe((response: any) => {
      this.categories = response;
    }, (error: any) => {
      console.log(error);
      super.buildResponseMessageFrom(error);
      this.snackBarService.openSnackBar(this.responseMessage, GlobalConstants.error);
    })
  }

  private getCategories() {
    return this.subscribeForCategories(this.categoryService.getCategories());
  }

  private edit() {
    const formData = this.productForm.value;
    const data = {
      id: this.dialogData.data.id,
      name: formData.name,
      categoryId: formData.category.id,
      price: formData.price,
      description: formData.description,
      status: formData.status
    };
    this.subscribe(this.productService.updateProduct(data));
  }

  private add() {
    const formData = this.productForm.value;
    const data = {
      name: formData.name,
      categoryId: formData.categoryId,
      price: formData.price,
      description: formData.description,
      status: formData.status
    };
    this.subscribe(this.productService.addProduct(data));
  }

}
