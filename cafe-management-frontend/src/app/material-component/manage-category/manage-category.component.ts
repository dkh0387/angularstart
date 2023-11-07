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
    dialogConfig.data = {data: data, message: "delete the category?", confirmation: "Delete"};
    const dialogRef = this.dialog.open(ConfirmationComponent, dialogConfig);
    const sub = this.subscribeForDelete(dialogRef, data);
  }

  setCategoryService(categoryService: CategoryService) {
    this.categoryService = categoryService;
  }

  setDialog(matDialog: any) {
    this.dialog = matDialog;
  }

  private subscribeForDelete(dialogRef: MatDialogRef<ConfirmationComponent>, data: any) {
    return dialogRef.componentInstance.onEmitStatusChange.subscribe((response) => {
      dialogRef.close();
      this.categoryService.deleteCategory(data).subscribe((response: any) => {
        this.ngxService.stop();
        this.snackBarService.openSnackBar(response, GlobalConstants.success);
        this.tableData();
      }, (error: any) => {
        this.ngxService.stop();
        console.log(error.error?.message);
        super.buildResponseMessageFrom(error);
        this.snackBarService.openSnackBar(this.responseMessage, GlobalConstants.error);
      });
    });
  }
}
