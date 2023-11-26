import {Component, OnInit} from '@angular/core';
import {MatDialog, MatDialogConfig} from "@angular/material/dialog";
import {SignupComponent} from "../signup/signup.component";
import {ForgotPasswordComponent} from "../forgot-password/forgot-password.component";
import {UserService} from "../services/user.service";
import {Router} from "@angular/router";
import {RestSubscriber} from "../interfaces/rest-subscriber";
import {Observable} from "rxjs";
import {GlobalConstants} from "../shared/global-constants";
import {TranslateService} from "@ngx-translate/core";

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss']
})
export class HomeComponent implements OnInit, RestSubscriber {
  aboutMePath = "/" + GlobalConstants.aboutMePath;
  mainPageTitle = GlobalConstants.mainPageTitle;
  mainPageIcon = GlobalConstants.mainPageIcon;
  headerIconChangeLanguage = GlobalConstants.headerIconChangeLanguage;

  constructor(private translateService: TranslateService, private dialog: MatDialog, private userService: UserService, private router: Router) {
    translateService.setDefaultLang(GlobalConstants.RUS);
    translateService.use(GlobalConstants.RUS);
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
      this.router.navigate([this.aboutMePath]);
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

  handleGoToServicesPage() {
    //this.router.navigate(["/" + GlobalConstants.aboutMePath]).catch((error) => console.log(error));
  }

  useLanguage(language: string): void {
    this.translateService.use(language);
  }

  handleGoToProjectsPage() {

  }

  handleGoToContactPage() {

  }
}
