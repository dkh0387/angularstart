import {Component, AfterViewInit} from '@angular/core';
import {DashboardService} from "../services/dashboard.service";
import {NgxUiLoaderService} from "ngx-ui-loader";
import {SnackbarService} from "../services/snackbar.service";
import {RestSubscriber} from "../interfaces/rest-subscriber";
import {SubmitHandler} from "../interfaces/submit-handler";
import {Observable} from "rxjs";
import {GlobalConstants} from "../shared/global-constants";

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.scss']
})
export class DashboardComponent implements AfterViewInit, RestSubscriber, SubmitHandler {
  responseMessage: any;
  data: any;

  ngAfterViewInit() {
  }

  constructor(private dashboardService: DashboardService, private ngxService: NgxUiLoaderService, private snackBarService: SnackbarService) {
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
      this.responseMessage = (error.error?.message == null) ? (GlobalConstants.error) : error.error?.message;
      console.log(error);
      this.snackBarService.openSnackBar(this.responseMessage, GlobalConstants.error);
    });
  }

  private getDashboardData() {
    this.subscribe(this.dashboardService.getDetails())
  }
}
