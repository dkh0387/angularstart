import {TestBed} from '@angular/core/testing';

import {RouteGuardService} from './route-guard.service';
import {AuthService} from "./auth.service";
import {ActivatedRouteSnapshot, Router} from "@angular/router";
import {SnackbarService} from "./snackbar.service";
import {HttpClientTestingModule} from "@angular/common/http/testing";
import {GlobalConstants} from "../shared/global-constants";

describe('RouteGuardService', () => {
  let service: RouteGuardService;
  let mockRouter = jasmine.createSpyObj('Router', ['navigate']);
  let mockSnackbarService = jasmine.createSpyObj('SnackbarService', ['openSnackBar']);
  let mockAuthService: AuthService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [{provide: Router, useValue: mockRouter},
        {provide: SnackbarService, useValue: mockSnackbarService}]
    });
    mockAuthService = new AuthService(mockRouter);
    service = TestBed.inject(RouteGuardService);
    service.authService = mockAuthService;
    service.router = mockRouter;
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should throw an exception if mockAuthService#getToken returns null', () => {

    const activatedRouteSnapshot = new ActivatedRouteSnapshot();
    activatedRouteSnapshot.data = {'expectedRole': [GlobalConstants.roleUser, GlobalConstants.roleAdmin]};
    const spyGetToken = spyOn(mockAuthService, 'getToken').and.returnValue(null);

    expect(() => {
      service.canActivate(activatedRouteSnapshot)
    }).toThrowError('Invalid token specified');
  });

  it('should return false if the user role decoded from token does not match any role from expected role array', () => {

    const activatedRouteSnapshot = new ActivatedRouteSnapshot();
    activatedRouteSnapshot.data = {'expectedRole': [GlobalConstants.roleUser + 'AAA', GlobalConstants.roleAdmin + 'BBB']};
    const spyGetToken = spyOn(mockAuthService, 'getToken').and.returnValue('eyJhbGciOiJFUzI1NiJ9.eyJpc3MiOiJodHRwczovL2xvY2FsaG9zdDo4MDgxIiwic3ViIjoiZGVuaXNraDg3QGdtYWlsLmNvbSIsInJvbGUiOiJST0xFX0FETUlOIiwiZXhwIjoxNjk4MDIzMzI4LCJpYXQiOjE2OTc5ODczMjh9.q_KDqHX02YAAONSod9iJZ32f9ijbpmC4f0v-Tlhmzlk8gxnkKyIu3QsIyVErq6WRAWgLx_zC_z3ytQsz4Ts1Sw');

    const canActivate = service.canActivate(activatedRouteSnapshot);

    expect(mockRouter.navigate()).toHaveBeenCalledWith(['/']);
    expect(canActivate).toEqual(false);
  });

  it('should return false if the user role decoded from token does match any role from expected role array but the user is not authenticated', () => {

    const activatedRouteSnapshot = new ActivatedRouteSnapshot();
    activatedRouteSnapshot.data = {'expectedRole': [GlobalConstants.roleUser, GlobalConstants.roleAdmin]};
    const spyGetToken = spyOn(mockAuthService, 'getToken').and.returnValue('eyJhbGciOiJFUzI1NiJ9.eyJpc3MiOiJodHRwczovL2xvY2FsaG9zdDo4MDgxIiwic3ViIjoiZGVuaXNraDg3QGdtYWlsLmNvbSIsInJvbGUiOiJST0xFX0FETUlOIiwiZXhwIjoxNjk4MDIzMzI4LCJpYXQiOjE2OTc5ODczMjh9.q_KDqHX02YAAONSod9iJZ32f9ijbpmC4f0v-Tlhmzlk8gxnkKyIu3QsIyVErq6WRAWgLx_zC_z3ytQsz4Ts1Sw');
    const spyIsAuthenticated = spyOn(mockAuthService, 'isAuthenticated').and.returnValue(false);
    const canActivate = service.canActivate(activatedRouteSnapshot);

    expect(mockRouter.navigate).toHaveBeenCalledWith(['/cafe/dashboard']);
    expect(canActivate).toEqual(false);
  });

  it('should return true if the user role decoded from token does match any role from expected role array and the user is authenticated', () => {

    const activatedRouteSnapshot = new ActivatedRouteSnapshot();
    activatedRouteSnapshot.data = {'expectedRole': [GlobalConstants.roleUser, GlobalConstants.roleAdmin]};
    const spyGetToken = spyOn(mockAuthService, 'getToken').and.returnValue('eyJhbGciOiJFUzI1NiJ9.eyJpc3MiOiJodHRwczovL2xvY2FsaG9zdDo4MDgxIiwic3ViIjoiZGVuaXNraDg3QGdtYWlsLmNvbSIsInJvbGUiOiJST0xFX0FETUlOIiwiZXhwIjoxNjk4MDIzMzI4LCJpYXQiOjE2OTc5ODczMjh9.q_KDqHX02YAAONSod9iJZ32f9ijbpmC4f0v-Tlhmzlk8gxnkKyIu3QsIyVErq6WRAWgLx_zC_z3ytQsz4Ts1Sw');
    const spyIsAuthenticated = spyOn(mockAuthService, 'isAuthenticated').and.returnValue(true);
    const canActivate = service.canActivate(activatedRouteSnapshot);

    expect(canActivate).toEqual(true);
  });
});
