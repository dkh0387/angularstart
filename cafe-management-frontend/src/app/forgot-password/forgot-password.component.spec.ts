import {ComponentFixture, TestBed} from '@angular/core/testing';

import {ForgotPasswordComponent} from './forgot-password.component';
import {UserService} from "../services/user.service";
import {ReactiveFormsModule} from "@angular/forms";
import {Router} from "@angular/router";
import {HttpClient} from "@angular/common/http";
import {SnackbarService} from "../services/snackbar.service";
import {MatDialogRef} from "@angular/material/dialog";
import {NgxUiLoaderService} from "ngx-ui-loader";
import {SignupComponent} from "../signup/signup.component";
import {Observable} from "rxjs";


/**
 * We use a “describe” to start our test block with the title matching the tested component name.
 */
describe('ForgotPasswordComponent', () => {
    /**
     * Mock objects being needed to initialize the test instance.
     */
    let mockRouter = jasmine.createSpyObj('Router', ['navigate']);
    let mockHttpClient = jasmine.createSpyObj('mockHttpClient', ['post'])
    let mockUserService: UserService; //= jasmine.createSpyObj('UserService', ['signUp']);
    let mockSnackbarService = jasmine.createSpyObj('SnackbarService', ['openSnackBar']);
    let mockMatDialogRef = jasmine.createSpyObj('MatDialogRef<ForgotPasswordComponent>', ['close']);
    let mockNgxUiLoaderService = jasmine.createSpyObj('NgxUiLoaderService', ['start', 'stop']);
    let component: ForgotPasswordComponent;
    let fixture: ComponentFixture<ForgotPasswordComponent>;
    /**
     * We use an async before each.
     * The purpose of the async is to let all the possible asynchronous code finish before continuing.
     * Before running any test in angular, you need to configure an angular testbed.
     * This allows you to create an angular environment for the component being tested.
     * Any module, component or service that your tested component needs have to be included in the testbed.
     * Finally, after setting the configuration, you call the compile components function.
     */
    beforeEach(async () => {
        await TestBed.configureTestingModule({
            declarations: [ForgotPasswordComponent],
            imports: [ReactiveFormsModule],
            providers: [{provide: Router, useValue: mockRouter},
                {provide: HttpClient, useValue: mockHttpClient},
                {provide: UserService, useValue: mockUserService},
                {provide: SnackbarService, useValue: mockSnackbarService},
                {provide: MatDialogRef, useValue: mockMatDialogRef},
                {provide: NgxUiLoaderService, useValue: mockNgxUiLoaderService}]
        })
            .compileComponents();
    });

    /**
     * Creating a new test object before each test.
     */
    beforeEach(() => {
        mockUserService = new UserService(mockHttpClient);
        fixture = TestBed.createComponent(ForgotPasswordComponent);
        component = fixture.componentInstance;
        fixture.detectChanges();
    });

    it('should create', () => {
        expect(component).toBeTruthy();
    });

    it('forgot password form should have field email created ', () => {
        const signupForm = fixture.debugElement.nativeElement.querySelector('#forgotPasswordForm');
        let inputFormFields = signupForm.querySelectorAll('mat-form-field');
        expect(inputFormFields.length).toEqual(1);
        expect(inputFormFields[0].id = 'email')
    });

    it('should require valid email', () => {
        component.forgotPasswordForm.setValue({
            'email': 'denisgmail.com'
        });

        expect(component.forgotPasswordForm.valid).toEqual(false);
    });

    it('should call forgot password from user service while handling submit if email is valid', () => {
        const formData = {
            'email': 'denis@gmail.com'
        };
        const data = {
            email: formData.email
        };
        component.forgotPasswordForm.setValue(formData);

        /**
         * Concept of using mocks: we define here what we want a spy to do on a specific method call.
         */
        const spyUserForgotPassword = spyOn(mockUserService, 'forgotPassword').and.returnValue(new Observable<Object>());
        component.setUserService(mockUserService);

        component.handleSubmit()

        expect(spyUserForgotPassword).toHaveBeenCalledWith(data);
    });

    it('should throw an error if the user service does while reset password', () => {
        const formData = {
            'email': 'denis@gmail.com'
        };
        component.forgotPasswordForm.setValue(formData);

        const spyUserSignUp = spyOn(mockUserService, 'forgotPassword').and.throwError('Error');
        component.setUserService(mockUserService);

        expect(() => {
            component.handleSubmit()
        }).toThrowError('Error');
    });
});

