import {Component, OnInit} from '@angular/core';
import {ItemManager} from "../extended/item-manager";
import {CategoryService} from "../services/category.service";
import {NgxUiLoaderService} from "ngx-ui-loader";
import {MatDialog} from "@angular/material/dialog";
import {SnackbarService} from "../services/snackbar.service";
import {Router} from "@angular/router";
import {GlobalConstants} from "../shared/global-constants";

@Component({
  selector: 'app-lena-services-overview',
  templateUrl: './lena-services-overview.component.html',
  styleUrls: ['./lena-services-overview.component.scss']
})
export class LenaServicesOverviewComponent implements OnInit {

  displayedColumns: string[] = ["name", "buy"];
  servicePageTitle = GlobalConstants.servicePageTitleRUS;

  constructor(private categoryService: CategoryService,
              ngxService: NgxUiLoaderService,
              dialog: MatDialog,
              snackBarService: SnackbarService,
              router: Router) {
  }

  ngOnInit(): void {
  }

  handleShowDetailsAction() {

  }
}
