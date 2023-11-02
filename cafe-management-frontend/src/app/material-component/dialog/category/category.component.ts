import {Component, EventEmitter, Inject, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {ResponseHadler} from "../../../extended/response-handler";
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";
import {CategoryService} from "../../../services/category.service";
import {SnackbarService} from "../../../services/snackbar.service";
import {SubmitHandler} from "../../../interfaces/submit-handler";
import {RestSubscriber} from "../../../interfaces/rest-subscriber";
import {Observable} from 'rxjs';
import {GlobalConstants} from "../../../shared/global-constants";

/**
 * Add a new / edit an existing product category.
 * This component is for embedding into a `MatDialog` element (see: ``manage-category.component.ts).
 */
@Component({
  selector: 'app-category',
  templateUrl: './category.component.html',
  styleUrls: ['./category.component.scss']
})
export class CategoryComponent extends ResponseHadler implements OnInit, SubmitHandler, RestSubscriber {

  onAddCategory = new EventEmitter();
  onEditCategory = new EventEmitter();
  categoryForm: any = FormGroup;
  dialogAction: any = GlobalConstants.dialogActionAdd;
  action: any = GlobalConstants.dialogActionAdd;

  constructor(@Inject(MAT_DIALOG_DATA) public dialogData: any,
              private formBuilder: FormBuilder,
              private categoryService: CategoryService,
              public dialogRef: MatDialogRef<CategoryComponent>,
              private snackBarService: SnackbarService) {
    super();
  }

  ngOnInit(): void {
    this.categoryForm = this.formBuilder.group({
      name: [null, [Validators.required]]
    });

    if (this.dialogData.action === GlobalConstants.dialogActionEdit) {
      this.dialogAction = GlobalConstants.dialogActionEdit;
      this.action = "update";
      this.categoryForm.patchValue(this.dialogData.data);
    }
  }

  subscribe(observable: Observable<Object>): void {
    observable.subscribe((response: any) => {
      this.dialogRef.close();

      if (this.dialogAction === GlobalConstants.dialogActionAdd) {
        this.onAddCategory.emit();
      } else {
        this.onEditCategory.emit();
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

  handleSubmit(): void {
    if (this.dialogAction === GlobalConstants.dialogActionEdit) {
      this.edit();
    } else {
      this.add();
    }
  }

  setCategoryService(categoryService: CategoryService) {
    this.categoryService = categoryService;
  }

  private edit() {
    const data = {id: this.dialogData.data.id, name: this.categoryForm.value.name};
    this.subscribe(this.categoryService.updateCategory(data));
  }

  private add() {
    const data = {name: this.categoryForm.value.name};
    this.subscribe(this.categoryService.addCategory(data));
  }

}
