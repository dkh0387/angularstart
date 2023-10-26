import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {UserService} from "../../../services/user.service";
import {MatDialogRef} from "@angular/material/dialog";
import {NgxUiLoaderService} from "ngx-ui-loader";
import {SnackbarService} from "../../../services/snackbar.service";
import {RestSubscriber} from "../../../interfaces/rest-subscriber";
import {SubmitHandler} from "../../../interfaces/submit-handler";
import {Observable} from 'rxjs';
import {GlobalConstants} from "../../../shared/global-constants";
import {Router} from "@angular/router";

@Component({
  selector: 'app-change-password',
  templateUrl: './change-password.component.html',
  styleUrls: ['./change-password.component.scss']
})
export class ChangePasswordComponent implements OnInit, RestSubscriber, SubmitHandler {

  oldPassword = true;
  newPassword = true;
  confirmPassword = true;
  changePasswordForm: any = FormGroup;
  responseMessage: any;

  constructor(
    private formBuilder: FormBuilder,
    private userService: UserService,
    public dialogRef: MatDialogRef<ChangePasswordComponent>,
    private ngxService: NgxUiLoaderService,
    private snackbarService: SnackbarService,
    private router: Router
  ) {
  }

  ngOnInit(): void {
    this.changePasswordForm = this.formBuilder.group({
      oldPassword: [null, Validators.required],
      newPassword: [null, Validators.required],
      confirmPassword: [null, Validators.required]
    })
  }

  handleSubmit(): void {
    this.ngxService.start();
    const formData = this.changePasswordForm.value;
    const data = {
      oldPassword: formData.oldPassword, newPassword: formData.newPassword
    }
    this.subscribe(this.userService.changePassword(data));
  }

  subscribe(observable: Observable<Object>): void {
    observable.subscribe((response: any) => {
      this.ngxService.stop();
      this.dialogRef.close();
      this.responseMessage = response;
      this.snackbarService.openSnackBar(this.responseMessage, ""); // pop up a green or black message depending on success
      localStorage.clear();
      this.router.navigate(['/']);
    }, (error) => {
      console.log(error)
      this.ngxService.stop();
      // show the error message
      this.responseMessage = (error.error?.message == null) ? (GlobalConstants.error) : error.error?.message;
      this.snackbarService.openSnackBar(this.responseMessage, GlobalConstants.error);
    });
  }

  validSubmit() {
    return this.changePasswordForm.controls['newPassword'].value == this.changePasswordForm.controls['confirmPassword'].value
  }

}
