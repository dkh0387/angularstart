import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {Router} from "@angular/router";
import {UserService} from "../services/user.service";
import {SnackbarService} from "../services/snackbar.service";
import {MatDialogRef} from "@angular/material/dialog";
import {NgxUiLoaderService} from "ngx-ui-loader";
import {GlobalConstants} from "../shared/global-constants";
import {Observable} from "rxjs";
import {RestSubscriber} from "../interfaces/rest-subscriber";
import {SubmitHandler} from "../interfaces/submit-handler";
import {ResponseHadler} from "../extended/response-handler";


@Component({
  selector: 'app-forgot-password',
  templateUrl: './forgot-password.component.html',
  styleUrls: ['./forgot-password.component.scss']
})
export class ForgotPasswordComponent extends ResponseHadler implements OnInit, RestSubscriber, SubmitHandler {
  forgotPasswordForm: any = FormGroup;

  constructor(private formBuilder: FormBuilder,
              private router: Router,
              private userService: UserService,
              private snackbarService: SnackbarService,
              public dialogRef: MatDialogRef<ForgotPasswordComponent>,
              private ngxService: NgxUiLoaderService) {
    super();
  }

  subscribe(observable: Observable<Object>): void {
    observable.subscribe((response: any) => {
      this.ngxService.stop();
      this.dialogRef.close();
      this.responseMessage = response;
      this.snackbarService.openSnackBar(this.responseMessage, ""); // pop up a green or black message depending on success
      this.router.navigate(["/"]); // after sign up navigate to the same page
    }, (error) => {
      this.ngxService.stop();
      // show the error message
      super.buildResponseMessageFrom(error);
      this.snackbarService.openSnackBar(this.responseMessage, GlobalConstants.error);
    });
  }

  ngOnInit(): void {
    this.forgotPasswordForm = this.formBuilder.group({
      email: [null, [Validators.required, Validators.pattern(GlobalConstants.emailRegex)]]
    })
  }

  handleSubmit() {
    this.ngxService.start();
    const formData = this.forgotPasswordForm.value;
    const data = {email: formData.email};
    this.subscribe(this.userService.forgotPassword(data));
  }

  setUserService(userService: UserService) {
    this.userService = userService;
  }
}
