import {ResponseHadler} from "./response-handler";
import {Injectable, OnInit} from "@angular/core";
import {RestSubscriber} from "../interfaces/rest-subscriber";
import {Observable} from "rxjs";
import {MatTableDataSource} from "@angular/material/table";
import {GlobalConstants} from "../shared/global-constants";
import {NgxUiLoaderService} from "ngx-ui-loader";
import {MatDialog, MatDialogRef} from "@angular/material/dialog";
import {SnackbarService} from "../services/snackbar.service";
import {Router} from "@angular/router";
import {ConfirmationComponent} from "../material-component/dialog/confirmation/confirmation.component";

/**
 * Base class for managing pages of all items like categories, products, etc., which have a table structure with actions.
 */
@Injectable()
export class ItemManager extends ResponseHadler implements OnInit, RestSubscriber {

  displayedColumns: string[] | undefined
  dataSource: any;

  constructor(protected ngxService: NgxUiLoaderService,
              protected dialog: MatDialog,
              protected snackbarService: SnackbarService,
              private router: Router) {
    super();
  }

  ngOnInit(): void {
    this.ngxService.start();
    this.loadData();
  }

  subscribe(observable: Observable<Object>): void {
    observable.subscribe((response: any) => {
        this.ngxService.stop();
        this.dataSource = new MatTableDataSource(response);
      }, (error: any) => {
        this.ngxService.stop();
        super.logAndShowError(error, this.snackbarService);
      }
    )
  }

  applyFilter(event: Event) {
    const filterValue = (event.target as HTMLInputElement).value;
    this.dataSource.filter = filterValue.trim().toLowerCase();
  }

  protected subscribeForDelete(dialogRef: MatDialogRef<ConfirmationComponent>, data: any, observable: Observable<Object>) {
    return dialogRef.componentInstance.onEmitStatusChange.subscribe((response) => {
      this.ngxService.start();
      observable.subscribe((response: any) => {
        this.ngxService.stop();
        this.snackbarService.openSnackBar(response, GlobalConstants.success);
        this.loadData();
      }, (error: any) => {
        this.ngxService.stop();
        super.logAndShowError(error, this.snackbarService);
      });
      dialogRef.close();
    });
  }

  protected loadData() {
    // to override...
  }

  protected updateDataSource() {
    this.dataSource = [...this.dataSource];
  }

  protected reset() {
    this.dataSource = [];
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

  handleDropAction(data: any, element: any) {
    // to override...
  }

}
