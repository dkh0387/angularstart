import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {Router} from "@angular/router";
import {UserService} from "../services/user.service";
import {SnackbarService} from "../services/snackbar.service";
import {MatDialogRef} from "@angular/material/dialog";
import {NgxUiLoaderService} from "ngx-ui-loader";
import {Observable} from "rxjs";
import {GlobalConstants} from "../shared/global-constants";
import {RestSubscriber} from "../interfaces/rest-subscriber";
import {SubmitHandler} from "../interfaces/submit-handler";
import {ResponseHadler} from "../extended/response-handler";


@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent extends ResponseHadler implements OnInit, RestSubscriber, SubmitHandler {

  hide = true;
  loginForm: any = FormGroup;

  constructor(
    private formBuilder: FormBuilder,
    private router: Router,
    private userService: UserService,
    private snackbarService: SnackbarService,
    public dialogRef: MatDialogRef<LoginComponent>,
    private ngxService: NgxUiLoaderService) {
    super();
  }

  /**
   * NOTE: we store a JWT in the local storage here.
   * @param observable
   */
  subscribe(observable: Observable<Object>): void {
    observable.subscribe((response: any) => {
      this.ngxService.stop();
      this.dialogRef.close();
      localStorage.setItem('token', response.token)
      this.responseMessage = GlobalConstants.success;
      this.snackbarService.openSnackBar(this.responseMessage, ""); // pop up a green or black message depending on success
      this.router.navigate(["/cafe/dashboard"]); // after log in, navigate to the dashboard, see [app-routing.module.ts]
    }, (error) => {
      this.ngxService.stop();
      super.logAndShowError(error, this.snackbarService);
    });
  }

  ngOnInit(): void {
    this.loginForm = this.formBuilder
      .group({
        email: [null, [Validators.required, Validators.pattern(GlobalConstants.emailRegex)]],
        password: [null, [Validators.required]]
      })
  }

  handleSubmit() {
    this.ngxService.start();
    const formData = this.loginForm.value;
    const data = {
      email: formData.email,
      password: formData.password
    };
    // we send data to the service and receive a response from them using subscribe method
    this.subscribe(this.userService.login(data));
  }

  setUserService(userService: UserService) {
    this.userService = userService;
  }
}
