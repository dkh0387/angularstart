import {ComponentFixture, TestBed} from '@angular/core/testing';

import {UserService} from "../services/user.service";
import {ReactiveFormsModule} from "@angular/forms";
import {Router} from "@angular/router";
import {HttpClient} from "@angular/common/http";
import {SnackbarService} from "../services/snackbar.service";
import {MatDialogRef} from "@angular/material/dialog";
import {NgxUiLoaderService} from "ngx-ui-loader";
import {Observable} from "rxjs";

import {LoginComponent} from './login.component';


/**
 * We use a “describe” to start our test block with the title matching the tested component name.
 */
describe('LoginComponent', () => {
  /**
   * Mock objects being needed to initialize the test instance.
   */
  let mockRouter = jasmine.createSpyObj('Router', ['navigate']);
  let mockHttpClient = jasmine.createSpyObj('mockHttpClient', ['post'])
  let mockUserService: UserService; //= jasmine.createSpyObj('UserService', ['signUp']);
  let mockSnackbarService = jasmine.createSpyObj('SnackbarService', ['openSnackBar']);
  let mockMatDialogRef = jasmine.createSpyObj('MatDialogRef<LoginComponent>', ['close']);
  let mockNgxUiLoaderService = jasmine.createSpyObj('NgxUiLoaderService', ['start', 'stop']);
  let component: LoginComponent;
  let fixture: ComponentFixture<LoginComponent>;
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
      declarations: [LoginComponent],
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
    fixture = TestBed.createComponent(LoginComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('log in form should have fields email, password created ', () => {
    const loginForm = fixture.debugElement.nativeElement.querySelector('#loginForm');
    let inputFormFields = loginForm.querySelectorAll('mat-form-field');
    expect(inputFormFields.length).toEqual(2);
    expect(inputFormFields[0].id = 'email')
    expect(inputFormFields[1].id = 'password')
  });

  it('should require valid email', () => {
    component.loginForm.setValue({
      'email': 'denisgmail.com',
      'password': '11235813'
    });

    expect(component.loginForm.valid).toEqual(false);
  });

  it('should require a password', () => {
    component.loginForm.setValue({
      'email': 'denis@gmail.com'
    });

    expect(component.loginForm.valid).toEqual(false);
  });

  it('should call log in from user service while handling submit if email is valid and password set', () => {
    const formData = {
      'email': 'denis@gmail.com',
      'password': '11235813'
    };
    const data = {
      email: formData.email,
      password: formData.password
    };
    component.loginForm.setValue(formData);

    /**
     * Concept of using mocks: we define here what we want a spy to do on a specific method call.
     */
    const spyUserLogIn = spyOn(mockUserService, 'login').and.returnValue(new Observable<Object>());
    component.setUserService(mockUserService);

    component.handleSubmit()

    expect(spyUserLogIn).toHaveBeenCalledWith(data);
  });

  it('should throw an error if the user service does while log in', () => {
    const formData = {
      'email': 'denis@gmail.com',
      'password': '11235813'
    };
    component.loginForm.setValue(formData);

    const spyUserLogIn = spyOn(mockUserService, 'login').and.throwError('Error');
    component.setUserService(mockUserService);

    expect(() => {
      component.handleSubmit()
    }).toThrowError('Error');
  });
});


