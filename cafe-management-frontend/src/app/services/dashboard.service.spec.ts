import {TestBed} from '@angular/core/testing';
/**
 * We can mock the backend
 * by using the HttpTestingController This class allows testing that certain endpoints are called
 * (and how many times),
 * but also allows to faking responses through the flush method.
 */
import {HttpClientTestingModule, HttpTestingController} from '@angular/common/http/testing';

import {DashboardService} from './dashboard.service';
import {environment} from "../../environments/environment";
import {GlobalConstants} from "../shared/global-constants";

describe('DashboardService', () => {
  let httpTestingController: HttpTestingController;
  let service: DashboardService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule]
    });
    httpTestingController = TestBed.inject(HttpTestingController);
    service = TestBed.inject(DashboardService);
  });

  afterEach(() => {
    httpTestingController.verify();
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  /**
   * 1. We provide expectedData to the testRequest
   * 2. We call a backend endpoint using testRequest
   * 3. we expect the incoming data from the testRequest is being expectedData
   */
  it('#getDetails should return expected dashboard data', (done) => {
    const expectedData = {
      'category': 6,
      'product': 10,
      'bill': 3
    };

    service.getDetails().subscribe(data => {
      expect(data).toEqual(expectedData);
      done();
    });

    const testRequest = httpTestingController.expectOne(environment.apiUrl + "/dashboard/details");

    testRequest.flush(expectedData);
  });

  it('#getDetails should use GET to retrieve data', () => {
    service.getDetails().subscribe();

    const testRequest = httpTestingController.expectOne(environment.apiUrl + "/dashboard/details");

    expect(testRequest.request.method).toEqual('GET');
  });
});
