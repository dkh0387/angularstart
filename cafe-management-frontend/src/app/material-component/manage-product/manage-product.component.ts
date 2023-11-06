import {Component} from '@angular/core';
import {NgxUiLoaderService} from "ngx-ui-loader";
import {MatDialog, MatDialogConfig} from "@angular/material/dialog";
import {SnackbarService} from "../../services/snackbar.service";
import {Router} from "@angular/router";
import {ProductService} from "../../services/product.service";
import {ItemManager} from "../../extended/item-manager";
import {GlobalConstants} from "../../shared/global-constants";
import {ProductComponent} from "../dialog/product/product.component";

@Component({
  selector: 'app-manage-product',
  templateUrl: './manage-product.component.html',
  styleUrls: ['./manage-product.component.scss']
})
export class ManageProductComponent extends ItemManager {

  displayedColumns: string[] = ["name", "category", "description", "price", "edit"];

  constructor(private productService: ProductService,
              ngxService: NgxUiLoaderService,
              dialog: MatDialog,
              snackBarService: SnackbarService,
              router: Router) {
    super(ngxService, dialog, snackBarService, router);
  }

  tableData() {
    this.subscribe(this.productService.getProducts());
  }

  handleAddAction() {
    const dialogConfig = new MatDialogConfig();
    dialogConfig.width = GlobalConstants.wideDialogWidth;
    dialogConfig.data = {action: GlobalConstants.dialogActionAdd};
    const dialogRef = this.dialog.open(ProductComponent, dialogConfig);
    const sub = dialogRef.componentInstance.onAddProduct.subscribe((response) => {
      dialogRef.close();
      this.tableData();
    })
  }

  /*  handleEditAction(data: any) {
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
      const sub = dialogRef.componentInstance.onEmitStatusChange.subscribe((response) => {
        dialogRef.close();
        this.productService.deleteProduct(data).subscribe((response: any) => {
          this.ngxService.stop();
          this.snackBarService.openSnackBar(response, GlobalConstants.success);
          this.tableData();
        }, (error: any) => {
          this.ngxService.stop();
          console.log(error.error?.message);
          super.buildResponseMessageFrom(error);
          this.snackBarService.openSnackBar(this.responseMessage, GlobalConstants.error);
        });
      })
    }*/

  handleStatusChangeAction(checked: boolean, id: bigint) {

  }
}
