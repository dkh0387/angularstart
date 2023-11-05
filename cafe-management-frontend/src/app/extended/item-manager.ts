import {ResponseHadler} from "./response-handler";
import {Injectable, OnInit} from "@angular/core";
import {RestSubscriber} from "../interfaces/rest-subscriber";
import {Observable} from "rxjs";
import {MatTableDataSource} from "@angular/material/table";
import {GlobalConstants} from "../shared/global-constants";
import {NgxUiLoaderService} from "ngx-ui-loader";
import {MatDialog, MatDialogConfig} from "@angular/material/dialog";
import {SnackbarService} from "../services/snackbar.service";
import {Router} from "@angular/router";
import {CategoryComponent} from "../material-component/dialog/category/category.component";
import {ConfirmationComponent} from "../material-component/dialog/confirmation/confirmation.component";

/**
 * Base class for managing pages of all items like categories, products, etc.
 */
@Injectable()
export class ItemManager extends ResponseHadler implements OnInit, RestSubscriber {

  displayedColumns: string[] | undefined
  dataSource: any;

  constructor(protected ngxService: NgxUiLoaderService,
              protected dialog: MatDialog,
              protected snackBarService: SnackbarService,
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
        this.snackBarService.openSnackBar(this.responseMessage, GlobalConstants.error);
      }
    )
  }

  tableData() {
    // to override...
  }

  applyFilter(event: Event) {
    const filterValue = (event.target as HTMLInputElement).value;
    this.dataSource.filter = filterValue.trim().toLowerCase();
  }

  handleAddAction() {
    // to override...
  }

  handleEditAction(data: any) {
    // to override...
  }

  handleDeleteAction(data: any) {
    // to override...
  }

}
