import {Component} from '@angular/core';
import {CategoryService} from "../../services/category.service";
import {NgxUiLoaderService} from "ngx-ui-loader";
import {MatDialog, MatDialogConfig, MatDialogRef} from "@angular/material/dialog";
import {SnackbarService} from "../../services/snackbar.service";
import {Router} from "@angular/router";
import {GlobalConstants} from "../../shared/global-constants";
import {ConfirmationComponent} from "../dialog/confirmation/confirmation.component";
import {CategoryComponent} from "../dialog/category/category.component";
import {ItemManager} from "../../extended/item-manager";
import {Observable} from "rxjs";

@Component({
  selector: 'app-manage-category',
  templateUrl: './manage-category.component.html',
  styleUrls: ['./manage-category.component.scss']
})
export class ManageCategoryComponent extends ItemManager {

  displayedColumns: string[] = ["name", "edit"];

  constructor(private categoryService: CategoryService,
              ngxService: NgxUiLoaderService,
              dialog: MatDialog,
              snackBarService: SnackbarService,
              router: Router) {
    super(ngxService, dialog, snackBarService, router);
  }

  tableData() {
    this.subscribe(this.categoryService.getCategories());
  }

  /**
   * Binding the `CategoryComponent` by calling a new `MatDialog` instance.
   * Because of Injection, all data from the dialog will be transmitted to the component.
   */
  handleAddAction() {
    const dialogConfig = new MatDialogConfig();
    dialogConfig.width = GlobalConstants.dialogWidth;
    dialogConfig.data = {action: GlobalConstants.dialogActionAdd};
    const dialogRef = this.dialog.open(CategoryComponent, dialogConfig);
    // If the CategoryComponent fires onAddCategory EventEmitter we reload the whole table
    const sub = dialogRef.componentInstance.onAddCategory.subscribe((response) => {
      dialogRef.close();
      this.tableData();
    })
  }

  handleEditAction(data: any) {
    const dialogConfig = new MatDialogConfig();
    dialogConfig.data = {action: GlobalConstants.dialogActionEdit, data: data};
    const dialogRef = this.dialog.open(CategoryComponent, dialogConfig);
    const sub = dialogRef.componentInstance.onEditCategory.subscribe((response) => {
      dialogRef.close();
      this.tableData();
    })
  }

  handleDeleteAction(data: any) {
    const dialogConfig = new MatDialogConfig();
    dialogConfig.data = {data: data, message: `delete the category ${data.name}?`, confirmation: true};
    const dialogRef = this.dialog.open(ConfirmationComponent, dialogConfig);
    const sub = this.subscribeForDelete(dialogRef, data, this.categoryService.deleteCategory(data));
  }

  setCategoryService(categoryService: CategoryService) {
    this.categoryService = categoryService;
  }

  setDialog(matDialog: any) {
    this.dialog = matDialog;
  }

}
