/**
 TestBed is a powerful unit testing tool provided by angular, and it is initialized in test.ts file.
 */
import {ComponentFixture, TestBed} from '@angular/core/testing';

import {SignupComponent} from './signup.component';
import {ReactiveFormsModule} from '@angular/forms';
import {Router} from '@angular/router';
import {UserService} from '../services/user.service';
import {SnackbarService} from '../services/snackbar.service';
import {MatDialogRef} from '@angular/material/dialog';
import {NgxUiLoaderService} from 'ngx-ui-loader';

import {Observable} from 'rxjs';
import {HttpClient} from '@angular/common/http';

/**
 * We use a “describe” to start our test block with the title matching the tested component name.
 */
describe('SignupComponent', () => {
    /**
     * Mock objects being needed to initialize the test instance.
     */
    let mockRouter = jasmine.createSpyObj('Router', ['navigate']);
    let mockHttpClient = jasmine.createSpyObj('mockHttpClient', ['post'])
    let mockUserService: UserService; //= jasmine.createSpyObj('UserService', ['signUp']);
    let mockSnackbarService = jasmine.createSpyObj('SnackbarService', ['openSnackBar']);
    let mockMatDialogRef = jasmine.createSpyObj('MatDialogRef<SignupComponent>', ['close']);
    let mockNgxUiLoaderService = jasmine.createSpyObj('NgxUiLoaderService', ['start', 'stop']);
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

    it('should require valid name', () => {
        component.signupForm.setValue({
            'name': '#++#+',
            'contactNumber': '1234567890',
            'email': 'denis@gmail.com',
            'password': '123',
            'confirmPassword': '123'
        });

        expect(component.signupForm.valid).toEqual(false);
    });

    it('should require valid contact number', () => {
        component.signupForm.setValue({
            'name': 'Denis',
            'contactNumber': '123456',
            'email': 'denis@gmail.com',
            'password': '123',
            'confirmPassword': '123'
        });

        expect(component.signupForm.valid).toEqual(false);
    });

    it('should require valid email', () => {
        component.signupForm.setValue({
            'name': 'Denis',
            'contactNumber': '1234567890',
            'email': 'deni+#++',
            'password': '123',
            'confirmPassword': '123'
        });

        expect(component.signupForm.valid).toEqual(false);
    });

    it('should return true if all fields are valid', () => {
        component.signupForm.setValue({
            'name': 'Denis',
            'contactNumber': '1234567890',
            'email': 'denis@gmail.com',
            'password': '123',
            'confirmPassword': '123'
        });

        expect(component.signupForm.valid).toEqual(true);
    });

    it('should require confirmed passwords', () => {
        component.signupForm.setValue({
            'name': 'Denis',
            'contactNumber': '1234567890',
            'email': 'denis@gmail.com',
            'password': '123',
            'confirmPassword': '12345'
        });

        let passwordConfirmed = component.passwordConfirmed();

        expect(passwordConfirmed).toEqual(false);
    });

    it('should call sign up from user service while handling submit if all fields are valid', () => {
        const formData = {
            'name': 'Denis',
            'contactNumber': '1234567890',
            'email': 'denis@gmail.com',
            'password': '123',
            'confirmPassword': '12345'
        };
        const data = {
            name: formData.name,
            email: formData.email,
            contactNumber: formData.contactNumber,
            password: formData.password
        };
        component.signupForm.setValue(formData);

        /**
         * Concept of using mocks: we define here what we want a spy to do on a specific method call.
         */
        const spyUserSignUp = spyOn(mockUserService, 'signUp').and.returnValue(new Observable<Object>());
        component.setUserService(mockUserService);

        component.handleSubmit()

        expect(spyUserSignUp).toHaveBeenCalledWith(data);
    });

    it('should throw an error if the user service does while sign up', () => {
        const formData = {
            'name': 'Denis',
            'contactNumber': '1234567890',
            'email': 'denis@gmail.com',
            'password': '123',
            'confirmPassword': '12345'
        };
        component.signupForm.setValue(formData);

        const spyUserSignUp = spyOn(mockUserService, 'signUp').and.throwError('Error');
        component.setUserService(mockUserService);

        expect(() => {
            component.handleSubmit()
        }).toThrowError('Error');
    });
});
