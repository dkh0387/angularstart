import {Component, Input} from '@angular/core';
import {GlobalConstants} from "../shared/global-constants";
import {MatDialog, MatDialogConfig} from "@angular/material/dialog";
import {BuyComponent} from "../material-component/dialog/buy/buy.component";
import {ItemManager} from "../extended/item-manager";
import {BuyService} from "../services/buy.service";
import {NgxUiLoaderService} from "ngx-ui-loader";
import {SnackbarService} from "../services/snackbar.service";
import {Router} from "@angular/router";
import {TranslateService} from "@ngx-translate/core";

@Component({
  selector: 'app-best-seller',
  templateUrl: './best-seller.component.html',
  styleUrls: ['./best-seller.component.scss']
})
export class BestSellerComponent extends ItemManager {

  constructor(private translateService: TranslateService,
              private buyService: BuyService,
              ngxService: NgxUiLoaderService,
              dialog: MatDialog,
              snackBarService: SnackbarService,
              router: Router) {
    super(ngxService, dialog, snackBarService, router);
  }

  loadData() {
    this.ngxService.stop();
  }

  handleShowBuyForm(key: string) {
    this.translateService.get(key).subscribe((res: string) => {
      this.openBuyDialog(res);
    });
  }

  private openBuyDialog(bestsellerTitle: string | undefined) {
    const dialogConfig = new MatDialogConfig();
    dialogConfig.width = GlobalConstants.dialogWidth;
    dialogConfig.data = {bestseller: bestsellerTitle};
    const dialogRef = this.dialog.open(BuyComponent, dialogConfig);
    this.router.events.subscribe(() => {
      dialogRef.close();
    });
  }
}
