import {Component} from '@angular/core';
import {NgxUiLoaderService} from "ngx-ui-loader";
import {MatDialog, MatDialogConfig, MatDialogRef} from "@angular/material/dialog";
import {SnackbarService} from "../../services/snackbar.service";
import {Router} from "@angular/router";
import {ProductService} from "../../services/product.service";
import {ItemManager} from "../../extended/item-manager";
import {GlobalConstants} from "../../shared/global-constants";
import {ProductComponent} from "../dialog/product/product.component";
import {ConfirmationComponent} from "../dialog/confirmation/confirmation.component";

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

  handleEditAction(data: any) {
    const dialogConfig = new MatDialogConfig();
    dialogConfig.data = {action: GlobalConstants.dialogActionEdit, data: data};
    const dialogRef = this.dialog.open(ProductComponent, dialogConfig);
    const sub = dialogRef.componentInstance.onEditProduct.subscribe((response) => {
      dialogRef.close();
      this.tableData();
    })
  }

  handleDeleteAction(data: any) {
    const dialogConfig = new MatDialogConfig();
    dialogConfig.data = {data: data, message: `delete the product ${data.name}?`, confirmation: true};
    const dialogRef = this.dialog.open(ConfirmationComponent, dialogConfig);
    const sub = this.subscribeForDelete(dialogRef, data, this.productService.deleteProduct(data));
  }

  handleStatusChangeAction(checked: boolean, id: bigint) {
    this.ngxService.start();
    const data = {id: id, status: checked};
    this.subscribeForUpdateStatus(data);
  }

  private subscribeForUpdateStatus(data: any) {
    this.ngxService.start();
    return this.productService.updateProductStatus(data).subscribe((response: any) => {
      this.ngxService.stop();
      this.snackbarService.openSnackBar(response, GlobalConstants.success);
      this.tableData();
    }, (error: any) => {
      this.ngxService.stop();
      super.logAndShowError(error, this.snackbarService);
    });
  }

}
