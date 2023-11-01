import {Component, OnInit} from '@angular/core';
import {CategoryService} from "../../services/category.service";
import {NgxUiLoaderService} from "ngx-ui-loader";
import {MatDialog} from "@angular/material/dialog";
import {SnackbarService} from "../../services/snackbar.service";
import {Router} from "@angular/router";
import {RestSubscriber} from "../../interfaces/rest-subscriber";
import {Observable} from 'rxjs';
import {MatTableDataSource} from "@angular/material/table";
import {ResponseHadler} from "../../extended/response-handler";
import {GlobalConstants} from "../../shared/global-constants";

@Component({
  selector: 'app-manage-category',
  templateUrl: './manage-category.component.html',
  styleUrls: ['./manage-category.component.scss']
})
export class ManageCategoryComponent extends ResponseHadler implements OnInit, RestSubscriber {

  displayedColumns: string[] = ['name', 'edit'];
  dataSource: any;

  constructor(private categoryService: CategoryService,
              private ngxService: NgxUiLoaderService,
              private dialog: MatDialog,
              private snackBarService: SnackbarService,
              private router: Router) {
    super();
  }

  ngOnInit(): void {
    this.ngxService.start();
    this.tableData();
  }

  subscribe(observable: Observable<Object>): void {
    observable.subscribe((response: any) => {
        this.ngxService.stop();
        this.dataSource = new MatTableDataSource(response);
      }, (error: any) => {
        this.ngxService.stop();
        console.log(error.error?.message);
        super.buildResponseMessageFrom(error);
        this.snackBarService.openSnackBar(super.responseMessage, GlobalConstants.error);
      }
    )
  }

  tableData() {
    this.subscribe(this.categoryService.getCategories());
  }

  applyFilter(event: Event) {
    const filterValue = (event.target as HTMLInputElement).value;
    this.dataSource.filter = filterValue.trim().toLowerCase();
  }

  handleAddAction() {

  }

  handleEditAction(data: any) {

  }
}
