import {Component, OnInit} from '@angular/core';
import {MatDialog, MatDialogConfig} from "@angular/material/dialog";
import {SignupComponent} from "../signup/signup.component";
import {ForgotPasswordComponent} from "../forgot-password/forgot-password.component";
import {LoginComponent} from "../login/login.component";
import {UserService} from "../services/user.service";
import {Router} from "@angular/router";
import {RestSubscriber} from "../interfaces/rest-subscriber";
import {Observable} from "rxjs";
import {GlobalConstants} from "../shared/global-constants";
import {FormGroup} from "@angular/forms";
import {CategoryComponent} from "../material-component/dialog/category/category.component";
import {BuyComponent} from "../material-component/dialog/buy/buy.component";

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss']
})
export class HomeComponent implements OnInit, RestSubscriber {

  dashboardPath = "/" + GlobalConstants.homePath + "/" + GlobalConstants.dashboardPath;
  mainPageTitle = GlobalConstants.mainPageTitle;
  mainPageIcon = GlobalConstants.mainPageIcon;
  /*
   * Language Settings
   */
  headerIconChangeLanguage = GlobalConstants.headerIconChangeLanguage;
  mainPageMenuAboutTextActual = GlobalConstants.mainPageMenuAboutTextRUS;
  mainPageMenuProjectsTextActual = GlobalConstants.mainPageMenuProjectsTextRUS;
  mainPageMenuContactTextActual = GlobalConstants.mainPageMenuContactTextRUS;

  mainPageMenuChangeLanguageGERTitle = GlobalConstants.mainPageMenuChangeLanguageGERTitle;
  mainPageMenuChangeLanguageRUSTitle = GlobalConstants.mainPageMenuChangeLanguageRUSTitle;

  // Best sellers
  servicePageTitle = GlobalConstants.servicePageTitleRUS;
  servicePageService1 = GlobalConstants.servicePageService1RUS;
  servicePageService2 = GlobalConstants.servicePageService2RUS;
  servicePageService3 = GlobalConstants.servicePageService3RUS;
  servicePageService4 = GlobalConstants.servicePageService4RUS;
  servicePageService5 = GlobalConstants.servicePageService5RUS;
  servicePageService6 = GlobalConstants.servicePageService6RUS;
  servicePageService7 = GlobalConstants.servicePageService7RUS;

  constructor(private dialog: MatDialog, private userService: UserService, private router: Router) {
  }

  /**
   * Trick here: if we are logged in and a token is stored,
   * we call a checkToken endpoint from backend, which is only reachable for a valid user role,
   * so we are implicitly proving the token to exist.
   */
  ngOnInit(): void {
    this.subscribe(this.userService.checkToken())
  }

  /**
   * Whenever an JWT exists, we are able to navigate from the home page to the dashboard (user or admin only).
   * @param observable
   */
  subscribe(observable: Observable<Object>): void {
    observable.subscribe(() => {
      this.router.navigate([this.dashboardPath]); // after check token, navigate to the dashboard,
      // see [app-routing.module.ts]
    }, (error) => {
      // log error
      console.log(error);
    });
  }

  /*
  Open a modal sign-up dialog.
  Usage in the according .html file.
   */
  handleSignUpAction() {
    const dialogConfig = new MatDialogConfig();
    dialogConfig.width = '550px';
    this.dialog.open(SignupComponent, dialogConfig);
  }

  handleForgotPasswordAction() {
    const dialogConfig = new MatDialogConfig();
    dialogConfig.width = '550px';
    this.dialog.open(ForgotPasswordComponent, dialogConfig);
  }

  handleLoginAction() {
    const dialogConfig = new MatDialogConfig();
    dialogConfig.width = '550px';
    this.dialog.open(LoginComponent, dialogConfig);
  }

  changeToGER() {
    this.mainPageMenuAboutTextActual = GlobalConstants.mainPageMenuAboutTextGER;
    this.mainPageMenuProjectsTextActual = GlobalConstants.mainPageMenuProjectsTextGER;
    this.mainPageMenuContactTextActual = GlobalConstants.mainPageMenuContactTextGER;
    this.servicePageTitle = GlobalConstants.servicePageTitleGER;
    this.servicePageService1 = GlobalConstants.servicePageService1GER;
    this.servicePageService2 = GlobalConstants.servicePageService2GER;
    this.servicePageService3 = GlobalConstants.servicePageService3GER;
    this.servicePageService4 = GlobalConstants.servicePageService4GER;
    this.servicePageService5 = GlobalConstants.servicePageService5GER;
    this.servicePageService6 = GlobalConstants.servicePageService6GER;
    this.servicePageService7 = GlobalConstants.servicePageService7GER;
  }

  changeToRUS() {
    this.mainPageMenuAboutTextActual = GlobalConstants.mainPageMenuAboutTextRUS;
    this.mainPageMenuProjectsTextActual = GlobalConstants.mainPageMenuProjectsTextRUS;
    this.mainPageMenuContactTextActual = GlobalConstants.mainPageMenuContactTextRUS;
    this.servicePageTitle = GlobalConstants.servicePageTitleRUS;
    this.servicePageService1 = GlobalConstants.servicePageService1RUS;
    this.servicePageService2 = GlobalConstants.servicePageService2RUS;
    this.servicePageService3 = GlobalConstants.servicePageService3RUS;
    this.servicePageService4 = GlobalConstants.servicePageService4RUS;
    this.servicePageService5 = GlobalConstants.servicePageService5RUS;
    this.servicePageService6 = GlobalConstants.servicePageService6RUS;
    this.servicePageService7 = GlobalConstants.servicePageService7RUS;
  }

  protected readonly FormGroup = FormGroup;
}
