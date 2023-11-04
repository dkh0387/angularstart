import {ComponentFixture, TestBed} from '@angular/core/testing';

import {ManageCategoryComponent} from './manage-category.component';
import {CategoryService} from "../../services/category.service";
import {ReactiveFormsModule} from "@angular/forms";
import {Router} from "@angular/router";
import {HttpClient} from "@angular/common/http";
import {SnackbarService} from "../../services/snackbar.service";
import {MatDialog, MatDialogConfig, MatDialogRef} from "@angular/material/dialog";
import {NgxUiLoaderService} from "ngx-ui-loader";
import {BehaviorSubject, Observable} from "rxjs";
import {MatTableModule} from "@angular/material/table";
import {CategoryComponent} from "../dialog/category/category.component";
import {EventEmitter} from "@angular/core";
import {GlobalConstants} from "../../shared/global-constants";
import {ConfirmationComponent} from "../dialog/confirmation/confirmation.component";

describe('ManageCategoryComponent', () => {
  let component: ManageCategoryComponent;
  let fixture: ComponentFixture<ManageCategoryComponent>;
  let mockRouter = jasmine.createSpyObj("Router", ["navigate"]);
  let mockHttpClient = jasmine.createSpyObj("mockHttpClient", ["post", "get"])
  let mockCategoryService: CategoryService;
  let mockSnackbarService = jasmine.createSpyObj("SnackbarService", ["openSnackBar"]);
  let mockMatDialog: any //= jasmine.createSpyObj("MatDialog", ["open"]);
  let mockMatDialogRef: any//= jasmine.createSpyObj(" MatDialogRef<CategoryComponent>", ["close"]);
  let mockNgxUiLoaderService = jasmine.createSpyObj("NgxUiLoaderService", ["start", "stop"]);
  let mockObservable = jasmine.createSpyObj("Observable<Object>", ["subscribe"]);

  let getCategorySubject = new BehaviorSubject([{id: 1, name: "Testcategory"}]);
  let spyGetCategories: jasmine.Spy;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ManageCategoryComponent],
      imports: [ReactiveFormsModule, MatTableModule],
      providers: [{provide: Router, useValue: mockRouter},
        {provide: HttpClient, useValue: mockHttpClient},
        {provide: CategoryService, useValue: mockCategoryService},
        {provide: SnackbarService, useValue: mockSnackbarService},
        {provide: MatDialog, useValue: mockMatDialog},
        {provide: MatDialogRef, useValue: mockMatDialogRef},
        {provide: NgxUiLoaderService, useValue: mockNgxUiLoaderService},
        {provide: Observable, useValue: mockObservable}]
    })
      .compileComponents();
  });

  function setUp(spyGetCategoriesError: boolean, spyDeleteCategoryError: boolean) {
    if (!spyGetCategoriesError) {
      spyGetCategories = spyOn(mockCategoryService, "getCategories").and.returnValue(getCategorySubject.asObservable());
    } else {
      spyGetCategories = spyOn(mockCategoryService, "getCategories").and.throwError("Error!");
    }

    if (!spyDeleteCategoryError) {
      spyGetCategories = spyOn(mockCategoryService, "deleteCategory").and.returnValue(getCategorySubject.asObservable());
    } else {
      spyGetCategories = spyOn(mockCategoryService, "deleteCategory").and.throwError("Error!");
    }

    mockMatDialogRef = {
      componentInstance: {
        onAddCategory: new EventEmitter(), onEditCategory: new EventEmitter(), onEmitStatusChange: new EventEmitter()
      }, close: jasmine.createSpy()
    };
    mockMatDialog = {open: jasmine.createSpy()};

    mockMatDialog.open.and.returnValue(mockMatDialogRef);
  }

  beforeEach(() => {
    fixture = TestBed.createComponent(ManageCategoryComponent);
    component = fixture.componentInstance;
    mockCategoryService = new CategoryService(mockHttpClient);
    component.setCategoryService(mockCategoryService);
  });

  it('should create', () => {
    setUp(false, false);
    fixture.detectChanges();
    expect(component).toBeTruthy();
  });

  it('should call the endpoint for get all categories and load table data', () => {
    setUp(false, false);
    fixture.detectChanges();
    component.tableData();

    expect(mockCategoryService.getCategories).toHaveBeenCalled();
    expect(component.dataSource._renderData.value[0]).toEqual(getCategorySubject.value[0]);
  });

  it('should throw an exception if endpoint for get all categories does', () => {
    setUp(true, false);

    expect(() => {
      component.tableData()
    }).toThrowError("Error!");
    expect(component.dataSource).toBeUndefined();
  });

  it('should open an add category dialog and reload data if add action is provided', () => {
    setUp(false, false);
    component.setDialog(mockMatDialog);
    fixture.detectChanges();

    component.handleAddAction();

    const dialogConfig = new MatDialogConfig();
    dialogConfig.width = GlobalConstants.dialogWidth;
    dialogConfig.data = {action: GlobalConstants.dialogActionAdd};

    expect(mockMatDialog.open).toHaveBeenCalledWith(CategoryComponent, dialogConfig);

    mockMatDialogRef.componentInstance.onAddCategory.next("Add a new category");

    expect(mockCategoryService.getCategories).toHaveBeenCalled();
    expect(component.dataSource._renderData.value[0]).toEqual(getCategorySubject.value[0]);
  });

  it('should open an edit category dialog and reload data if edit action is provided', () => {
    setUp(false, false);
    component.setDialog(mockMatDialog);
    fixture.detectChanges();

    component.handleEditAction(getCategorySubject.value[0]);

    const dialogConfig = new MatDialogConfig();
    dialogConfig.data = {action: GlobalConstants.dialogActionEdit, data: getCategorySubject.value[0]};

    expect(mockMatDialog.open).toHaveBeenCalledWith(CategoryComponent, dialogConfig);

    mockMatDialogRef.componentInstance.onEditCategory.next("Edit a new category");

    expect(mockCategoryService.getCategories).toHaveBeenCalled();
    expect(component.dataSource._renderData.value[0]).toEqual(getCategorySubject.value[0]);
  });

  it('should open a delete category dialog and reload data if edit action is provided', () => {
    setUp(false, false);
    component.setDialog(mockMatDialog);
    fixture.detectChanges();

    component.handleDeleteAction(getCategorySubject.value[0]);

    const dialogConfig = new MatDialogConfig();
    dialogConfig.data = {data: getCategorySubject.value[0], message: "delete the category?", confirmation: "Delete"};

    expect(mockMatDialog.open).toHaveBeenCalledWith(ConfirmationComponent, dialogConfig);

    mockMatDialogRef.componentInstance.onEmitStatusChange.next("Delete a category");

    expect(mockCategoryService.deleteCategory).toHaveBeenCalledWith(getCategorySubject.value[0]);
  });

});
