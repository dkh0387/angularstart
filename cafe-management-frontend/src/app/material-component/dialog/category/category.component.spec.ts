import {ComponentFixture, TestBed} from '@angular/core/testing';

import {CategoryComponent} from './category.component';
import {ReactiveFormsModule} from "@angular/forms";
import {SnackbarService} from "../../../services/snackbar.service";
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";
import {CategoryService} from "../../../services/category.service";
import {GlobalConstants} from "../../../shared/global-constants";
import {By} from "@angular/platform-browser";
import {Observable} from "rxjs";
import {UserService} from "../../../services/user.service";
import {HttpClient} from "@angular/common/http";


describe('CategoryComponent', () => {
  let component: CategoryComponent;
  let fixture: ComponentFixture<CategoryComponent>;
  /**
   * Mock objects being needed to initialize the test instance.
   */
  let mockCategoryService: CategoryService;
  let mockHttpClient = jasmine.createSpyObj('mockHttpClient', ['post'])
  let mockSnackbarService = jasmine.createSpyObj('SnackbarService', ['openSnackBar']);
  let mockMatDialogRef = jasmine.createSpyObj('MatDialogRef<CategoryComponent>', ['close']);
  let dialogData = {"action": GlobalConstants.dialogActionEdit, "data": {"id": 1, "name": "Drinks"}};

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [CategoryComponent],
      imports: [ReactiveFormsModule],
      providers: [{provide: CategoryService, useValue: mockCategoryService},
        {provide: HttpClient, useValue: mockHttpClient},
        {provide: SnackbarService, useValue: mockSnackbarService},
        {provide: MatDialogRef, useValue: mockMatDialogRef},
        {provide: MAT_DIALOG_DATA, useValue: dialogData}]
    })
      .compileComponents();
  });

  beforeEach(() => {
    mockCategoryService = new CategoryService(mockHttpClient);
    fixture = TestBed.createComponent(CategoryComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('category form should have field name created ', () => {
    const categoryForm = fixture.debugElement.nativeElement.querySelector('#categoryForm');
    let inputFormFields = categoryForm.querySelectorAll('mat-form-field');
    expect(inputFormFields.length).toEqual(1);
    expect(inputFormFields[0].id = "name");
  });

  it('should require non empty name', () => {
    component.categoryForm.setValue({
      "name": null,
    });

    expect(component.categoryForm.valid).toEqual(false);
  });

  it('should be validated by non empty name', () => {
    component.categoryForm.setValue({
      "name": "Drinks",
    });

    expect(component.categoryForm.valid).toEqual(true);
  });

  it('should have a dialog title according to the corresponding dialogData', () => {
    const categoryForm = fixture.debugElement.nativeElement.querySelector('#categoryForm');
    const title = fixture.debugElement.query(By.css("#title")).nativeElement;
    expect(title.innerHTML).toBe(GlobalConstants.dialogActionEdit);
  });

  it('should call category service to edit an existing category', () => {
    const data = {"id": 1, "name": "Drinks"};
    component.categoryForm.setValue({
      "name": "Drinks",
    });

    const spyCategoryServiceUpdate = spyOn(mockCategoryService, 'updateCategory').and.returnValue(new Observable<Object>());
    component.setCategoryService(mockCategoryService);

    component.handleSubmit()

    expect(spyCategoryServiceUpdate).toHaveBeenCalledWith(data);
  });

  it('should throw an error if the category service does while update a category', () => {
    const data = {"id": 1, "name": "Drinks"};
    component.categoryForm.setValue({
      "name": "Drinks",
    });

    const spyCategoryServiceUpdate = spyOn(mockCategoryService, 'updateCategory').and.throwError("Error");
    component.setCategoryService(mockCategoryService);

    expect(() => {
      component.handleSubmit()
    }).toThrowError("Error");
  });
});
