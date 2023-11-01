import {Component, AfterViewInit} from '@angular/core';
import {DashboardService} from "../services/dashboard.service";
import {NgxUiLoaderService} from "ngx-ui-loader";
import {SnackbarService} from "../services/snackbar.service";
import {RestSubscriber} from "../interfaces/rest-subscriber";
import {SubmitHandler} from "../interfaces/submit-handler";
import {Observable} from "rxjs";
import {GlobalConstants} from "../shared/global-constants";
import {ResponseHadler} from "../extended/response-handler";

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.scss']
})
export class DashboardComponent extends ResponseHadler implements AfterViewInit, RestSubscriber, SubmitHandler {

  data: any;
  categoryPath = "/" + GlobalConstants.homePath + "/" + GlobalConstants.categoryPath
  productPath = "/" + GlobalConstants.homePath + "/" + GlobalConstants.productPath
  billPath = "/" + GlobalConstants.homePath + "/" + GlobalConstants.billPath

  ngAfterViewInit() {
  }

  constructor(private dashboardService: DashboardService,
              private ngxService: NgxUiLoaderService,
              private snackBarService: SnackbarService) {
    super();
    this.ngxService.start();
    this.getDashboardData();
  }

  handleSubmit(): void {
  }

  /**
   * Handling the incoming response from backend.
   *
   * @param observable
   */
  subscribe(observable: Observable<Object>): void {
    observable.subscribe((response: any) => {
      this.ngxService.stop();
      this.data = response;
    }, (error: any) => {
      this.ngxService.stop();
      // show the error message
      super.buildResponseMessageFrom(error);
      console.log(error);
      this.snackBarService.openSnackBar(this.responseMessage, GlobalConstants.error);
    });
  }

  private getDashboardData() {
    this.subscribe(this.dashboardService.getDetails())
  }
}
