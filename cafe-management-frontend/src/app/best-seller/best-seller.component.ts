import {Component, Input} from '@angular/core';
import {GlobalConstants} from "../shared/global-constants";
import {MatDialog, MatDialogConfig} from "@angular/material/dialog";
import {BuyComponent} from "../material-component/dialog/buy/buy.component";
import {ItemManager} from "../extended/item-manager";
import {BuyService} from "../services/buy.service";
import {NgxUiLoaderService} from "ngx-ui-loader";
import {SnackbarService} from "../services/snackbar.service";
import {Router} from "@angular/router";

@Component({
  selector: 'app-best-seller',
  templateUrl: './best-seller.component.html',
  styleUrls: ['./best-seller.component.scss']
})
export class BestSellerComponent extends ItemManager {
  @Input('app-best-seller-servicePageTitle') servicePageTitle: string | undefined;
  @Input('app-best-seller-servicePageService1') servicePageService1: string | undefined;
  @Input('app-best-seller-servicePageService2') servicePageService2: string | undefined;
  @Input('app-best-seller-servicePageService3') servicePageService3: string | undefined;
  @Input('app-best-seller-servicePageService4') servicePageService4: string | undefined;
  @Input('app-best-seller-servicePageService5') servicePageService5: string | undefined;
  @Input('app-best-seller-servicePageService6') servicePageService6: string | undefined;
  @Input('app-best-seller-servicePageService7') servicePageService7: string | undefined;

  constructor(private buyService: BuyService,
              ngxService: NgxUiLoaderService,
              dialog: MatDialog,
              snackBarService: SnackbarService,
              router: Router) {
    super(ngxService, dialog, snackBarService, router);
  }

  loadData() {
    this.ngxService.stop();
  }

  handleShowBuyForm(servicePageService: string | undefined) {
    switch (servicePageService) {
      case this.servicePageService1:
        this.openBuyDialog(this.servicePageService1);
        break;
      case this.servicePageService2:
        this.openBuyDialog(this.servicePageService2);
        break;
      case this.servicePageService3:
        this.openBuyDialog(this.servicePageService3);
        break;
      case this.servicePageService4:
        this.openBuyDialog(this.servicePageService4);
        break;
      case this.servicePageService5:
        this.openBuyDialog(this.servicePageService5);
        break;
      case this.servicePageService6:
        this.openBuyDialog(this.servicePageService6);
        break;
      case this.servicePageService7:
        this.openBuyDialog(this.servicePageService7);
        break;
    }
  }

  private openBuyDialog(servicePageService: string | undefined) {
    const dialogConfig = new MatDialogConfig();
    dialogConfig.width = GlobalConstants.dialogWidth;
    dialogConfig.data = {service: servicePageService};
    const dialogRef = this.dialog.open(BuyComponent, dialogConfig);
    this.router.events.subscribe(() => {
      dialogRef.close();
    });
  }
}
