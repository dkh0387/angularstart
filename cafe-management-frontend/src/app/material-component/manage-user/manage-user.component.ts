import {Component} from '@angular/core';
import {ItemManager} from "../../extended/item-manager";
import {NgxUiLoaderService} from "ngx-ui-loader";
import {MatDialog} from "@angular/material/dialog";
import {SnackbarService} from "../../services/snackbar.service";
import {Router} from "@angular/router";
import {UserService} from "../../services/user.service";
import {Observable} from "rxjs";

@Component({
  selector: 'app-manage-user',
  templateUrl: './manage-user.component.html',
  styleUrls: ['./manage-user.component.scss']
})
export class ManageUserComponent extends ItemManager {

  displayedColumns: string[] = ["name", "email", "contactNumber", "status", "edit"];

  constructor(private userService: UserService,
              ngxService: NgxUiLoaderService,
              dialog: MatDialog,
              snackBarService: SnackbarService,
              router: Router) {
    super(ngxService, dialog, snackBarService, router);
  }

  loadData() {
    this.subscribe(this.userService.getUsers());
  }

  handleStatusChangeAction(status: any, id: any) {
    this.ngxService.start();
    const data = {status: status.toString(), id: id};
    this.subscribeForStatusUpdate(this.userService.updateUser(data));
  }

  private subscribeForStatusUpdate(observable: Observable<Object>): void {
    observable.subscribe((response: any) => {
        this.ngxService.stop();
      }, (error: any) => {
        this.ngxService.stop();
        super.logAndShowError(error, this.snackbarService);
      }
    )
  }

}
