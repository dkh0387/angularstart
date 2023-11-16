import {Component} from '@angular/core';
import {ItemManager} from "../../extended/item-manager";
import {SubmitHandler} from "../../interfaces/submit-handler";
import {NgxUiLoaderService} from "ngx-ui-loader";
import {MatDialog, MatDialogConfig} from "@angular/material/dialog";
import {SnackbarService} from "../../services/snackbar.service";
import {Router} from "@angular/router";
import {BillService} from "../../services/bill.service";
import {GlobalConstants} from "../../shared/global-constants";
import {ViewBillProductsComponent} from "../dialog/view-bill-products/view-bill-products.component";
import {ConfirmationComponent} from "../dialog/confirmation/confirmation.component";

@Component({
  selector: 'app-view-bill',
  templateUrl: './view-bill.component.html',
  styleUrls: ['./view-bill.component.scss']
})
export class ViewBillComponent extends ItemManager implements SubmitHandler {

  displayedColumns: string[] = ["name", "email", "contactNumber", "paymentMethod", "total", "view"];

  constructor(private billService: BillService,
              ngxService: NgxUiLoaderService,
              dialog: MatDialog,
              snackbarService: SnackbarService,
              router: Router) {
    super(ngxService, dialog, snackbarService, router);
  }

  handleSubmit(): void {
  }

  handleViewAction(data: any) {
    const dialogConfig = new MatDialogConfig();
    dialogConfig.data = {data: data};
    dialogConfig.width = GlobalConstants.fullDialogWidth;
    const dialogRef = this.dialog.open(ViewBillProductsComponent, dialogConfig);
    this.router.events.subscribe(() => dialogRef.close());
  }

  handleDownloadBillAction(data: any) {
    this.ngxService.start();
    const uuidMap = {uuid: data.uuid};
    super.subscribeForBillDownload(this.billService.getBillDocument(uuidMap), data.uuid);
  }

  handleDeleteAction(data: any) {
    const dialogConfig = new MatDialogConfig();
    dialogConfig.data = {message: `delete the bill ${data?.name}`, confirmation: true};
    const dialogRef = this.dialog.open(ConfirmationComponent, dialogConfig);
    super.subscribeForDelete(dialogRef, this.billService.deleteBill(data));
  }

  protected loadData() {
    this.subscribe(this.billService.getBills());
  }

}
