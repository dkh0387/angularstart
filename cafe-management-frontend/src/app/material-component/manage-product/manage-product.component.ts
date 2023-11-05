import {Component} from '@angular/core';
import {NgxUiLoaderService} from "ngx-ui-loader";
import {MatDialog} from "@angular/material/dialog";
import {SnackbarService} from "../../services/snackbar.service";
import {Router} from "@angular/router";
import {ProductService} from "../../services/product.service";
import {ItemManager} from "../../extended/item-manager";

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

}
