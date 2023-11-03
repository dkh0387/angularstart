import {TestBed} from '@angular/core/testing';

import {CategoryService} from './category.service';
import {HttpClientTestingModule, HttpTestingController} from "@angular/common/http/testing";
import {environment} from "../../environments/environment";
import {GlobalConstants} from "../shared/global-constants";

describe('CategoryService', () => {
  let service: CategoryService;
  let httpTestingController: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule]
    });
    httpTestingController = TestBed.inject(HttpTestingController);
    service = TestBed.inject(CategoryService);
  });

  afterEach(() => {
    httpTestingController.verify();
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('#getCategories should return expected categories', (done) => {
    const expectedData = {
      "id": 6,
      "name": "Testcategory"
    };

    service.getCategories().subscribe(data => {
      expect(data).toEqual(expectedData);
      done();
    });

    const testRequest = httpTestingController.expectOne(environment.apiUrl + "/" + GlobalConstants.categoryPath + "/get");

    testRequest.flush(expectedData);
  });

  it('#getCategories should use GET to retrieve data', () => {
    service.getCategories().subscribe();

    const testRequest = httpTestingController.expectOne(environment.apiUrl + "/" + GlobalConstants.categoryPath + "/get");

    expect(testRequest.request.method).toEqual("GET");
  });

  it('#addCategory should return expected response', (done) => {
    const data = {
      "name": "Testcategory"
    };

    service.addCategory(data).subscribe(response => {
      expect(response).toEqual("New category successfully added!");
      done();
    });

    const testRequest = httpTestingController.expectOne(environment.apiUrl + "/" + GlobalConstants.categoryPath + "/add");

    testRequest.flush("New category successfully added!");
  });

  it('#addCategory should use POST to add a category', () => {
    const data = {
      "name": "Testcategory"
    };

    service.addCategory(data).subscribe();

    const testRequest = httpTestingController.expectOne(environment.apiUrl + "/" + GlobalConstants.categoryPath + "/add");

    expect(testRequest.request.method).toEqual("POST");
  });

  it('#updateCategory should return expected response', (done) => {
    const data = {
      "id": 1,
      "name": "Testcategory"
    };

    service.updateCategory(data).subscribe(response => {
      expect(response).toEqual("New category successfully updated!");
      done();
    });

    const testRequest = httpTestingController.expectOne(environment.apiUrl + "/" + GlobalConstants.categoryPath + "/update");

    testRequest.flush("New category successfully updated!");
  });

  it('#updateCategory should use POST to update a category', () => {
    const data = {
      "id": 1,
      "name": "Testcategory"
    };

    service.updateCategory(data).subscribe();

    const testRequest = httpTestingController.expectOne(environment.apiUrl + "/" + GlobalConstants.categoryPath + "/update");

    expect(testRequest.request.method).toEqual("POST");
  });

  it('#deleteCategory should return expected categories', (done) => {
    const data = {
      "id": 1
    };

    service.deleteCategory(data).subscribe(response => {
      expect(response).toEqual("Category successfully deleted!");
      done();
    });

    const testRequest = httpTestingController.expectOne(environment.apiUrl + "/" + GlobalConstants.categoryPath + "/delete/1");

    testRequest.flush("Category successfully deleted!");
  });

  it('#deleteCategory should use POST to delete a category', () => {
    const data = {
      "id": 1
    };

    service.deleteCategory(data).subscribe();

    const testRequest = httpTestingController.expectOne(environment.apiUrl + "/" + GlobalConstants.categoryPath + "/delete/1");

    expect(testRequest.request.method).toEqual("POST");
  });
});
