import {Component} from '@angular/core';
import {ItemManager} from "../../extended/item-manager";
import {NgxUiLoaderService} from "ngx-ui-loader";
import {MatDialog} from "@angular/material/dialog";
import {SnackbarService} from "../../services/snackbar.service";
import {Router} from "@angular/router";
import {OrderService} from "../../services/order.service";

@Component({
  selector: 'app-manage-order',
  templateUrl: './manage-order.component.html',
  styleUrls: ['./manage-order.component.scss']
})
export class ManageOrderComponent extends ItemManager {

  displayedColumns: string[] = ["name", "category", "price", "quantity", "total", "edit"];

  constructor(private orderService: OrderService,
              ngxService: NgxUiLoaderService,
              dialog: MatDialog,
              snackBarService: SnackbarService,
              router: Router) {
    super(ngxService, dialog, snackBarService, router);
  }

  tableData() {
    //this.subscribe(this.orderService.get...);
    this.ngxService.stop();
  }

}
