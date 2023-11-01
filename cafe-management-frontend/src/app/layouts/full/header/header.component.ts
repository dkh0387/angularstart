import {Component} from '@angular/core';
import {MatDialog, MatDialogConfig} from "@angular/material/dialog";
import {Router} from "@angular/router";
import {ConfirmationComponent} from "../../../material-component/dialog/confirmation/confirmation.component";
import {resolve} from "@angular/compiler-cli/src/ngtsc/file_system";
import {ChangePasswordComponent} from "../../../material-component/dialog/change-password/change-password.component";
import {GlobalConstants} from "../../../shared/global-constants";

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: []
})
export class AppHeaderComponent {

  role: any

  constructor(private router: Router, private dialog: MatDialog) {
  }

  /**
   * Logout process in the app header:
   * 1. We create a modal dialog instance (MatDialog) --> dialog frame
   * 2. We equip the dialog with ConfirmationComponent, which contains Yes/No dialog content
   * 3. ConfirmationComponent receives dialog data since we did: @Inject(MAT_DIALOG_DATA) public dialogData
   * 4. We detect all changes in the dialog by using onEmitStatusChange.
   * NOTE subscribe() here, it manages what to do after the dialog is confirmed
   * If logout is done, we close, remove token and redirect to the homepage
   */
  logout() {
    const dialogConfig = new MatDialogConfig();
    dialogConfig.data = {message: "logout", confirmation: true};
    const dialogRef = this.dialog.open(ConfirmationComponent, dialogConfig);
    const sub = dialogRef.componentInstance.onEmitStatusChange.subscribe((response) => {
      dialogRef.close();
      localStorage.clear();
      this.router.navigate(["/"]);
    })
  }

  changePassword() {
    const dialogConfig = new MatDialogConfig();
    dialogConfig.width = GlobalConstants.dialogWidth;
    this.dialog.open(ChangePasswordComponent, dialogConfig);
  }
}
