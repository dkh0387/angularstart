import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {UserService} from "../services/user.service";
import {Router} from "@angular/router";
import {SnackbarService} from "../services/snackbar.service";
import {MatDialogRef} from "@angular/material/dialog";
import {NgxUiLoaderService} from "ngx-ui-loader";
import {GlobalConstants} from "../shared/global-constants";
import {Observable} from "rxjs";
import {RestSubscriber} from "../interfaces/rest";


/**
 * Component for sig up a form, consisting of name, email, contact number and password.
 */
@Component({
    selector: 'app-signup',
    templateUrl: './signup.component.html',
    styleUrls: ['./signup.component.scss']
})
export class SignupComponent implements OnInit, RestSubscriber {

    password = true;
    confirmPassword = true;
    signupForm: any = FormGroup;
    responseMessage: any;

    constructor(
        private formBuilder: FormBuilder,
        private router: Router,
        private userService: UserService,
        private snackbarService: SnackbarService,
        public dialogRef: MatDialogRef<SignupComponent>,
        private ngxService: NgxUiLoaderService) {
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
            this.responseMessage = (error.error?.message == null) ? (GlobalConstants.error) : error.error?.message;
            this.snackbarService.openSnackBar(this.responseMessage, GlobalConstants.error);
        });
    }

    ngOnInit(): void {
        this.signupForm = this.formBuilder
            .group({
                name: [null, [Validators.required, Validators.pattern(GlobalConstants.nameRegex)]],
                email: [null, [Validators.required, Validators.pattern(GlobalConstants.emailRegex)]],
                contactNumber: [null, [Validators.required, Validators.pattern(GlobalConstants.contactNumberRegex)]],
                password: [null, [Validators.required]],
                confirmPassword: [null, [Validators.required]]
            })
    }

    /**
     * Validation of password match. NOTE: the access to single fields inside the form!
     */
    passwordConfirmed() {
        return this.signupForm.controls.password.value == this.signupForm.controls.confirmPassword.value;
    }

    handleSubmit() {
        this.ngxService.start();
        const formData = this.signupForm.value;
        const data = {
            name: formData.name,
            email: formData.email,
            contactNumber: formData.contactNumber,
            password: formData.password
        };
        // we send data to the service and receive a response from them using subscribe method
        this.subscribe(this.userService.signUp(data));
    }

    setUserService(userService: UserService) {
        this.userService = userService;
    }
}
