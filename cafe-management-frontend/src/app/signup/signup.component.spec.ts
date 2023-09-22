/**
 TestBed is a powerful unit testing tool provided by angular, and it is initialized in test.ts file.
 */
import {ComponentFixture, TestBed} from '@angular/core/testing';

import {SignupComponent} from './signup.component';
import {ReactiveFormsModule} from "@angular/forms";
import {Router} from "@angular/router";
import {UserService} from "../services/user.service";
import {SnackbarService} from "../services/snackbar.service";
import {MatDialogRef} from "@angular/material/dialog";
import {NgxUiLoaderService} from "ngx-ui-loader";

/**
 * We use a “describe” to start our test block with the title matching the tested component name.
 */
describe('SignupComponent', () => {
    /**
     * Mock objects being needed to initialize the test instance.
     */
    const mockRouter = jasmine.createSpyObj("Router", ["navigate"]);
    const mockUserService = jasmine.createSpyObj("UserService", ["signUp"]);
    const mockSnackbarService = jasmine.createSpyObj("SnackbarService", ["openSnackBar"]);
    const mockMatDialogRef = jasmine.createSpyObj("MatDialogRef<SignupComponent>", ["close"]);
    const mockNgxUiLoaderService = jasmine.createSpyObj("NgxUiLoaderService", ["start", "stop"]);
    let component: SignupComponent;
    let fixture: ComponentFixture<SignupComponent>;
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
            declarations: [SignupComponent],
            imports: [ReactiveFormsModule],
            providers: [{provide: Router, useValue: mockRouter},
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
        fixture = TestBed.createComponent(SignupComponent);
        component = fixture.componentInstance;
        fixture.detectChanges();
    });

    it('should create', () => {
        expect(component).toBeTruthy();
    });

    it('sign up form should have fields name, email, contact number, passwort and confirm passwort created ', () => {
        const signupForm = fixture.debugElement.nativeElement.querySelector('#signupForm');
        let inputFormFields = signupForm.querySelectorAll('mat-form-field');
        expect(inputFormFields.length).toEqual(5);
        expect(inputFormFields[0].id = 'name')
        expect(inputFormFields[1].id = 'contactNumber')
        expect(inputFormFields[2].id = 'email')
        expect(inputFormFields[3].id = 'password')
        expect(inputFormFields[4].id = 'confirmPassword')
    });
});
