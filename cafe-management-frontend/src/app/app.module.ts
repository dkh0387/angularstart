import {NgModule} from '@angular/core';
import {BrowserModule} from '@angular/platform-browser';

import {AppRoutingModule} from './app-routing.module';
import {AppComponent} from './app.component';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {MaterialModule} from './shared/material-module';
import {HomeComponent} from './home/home.component';
import {BestSellerComponent} from './best-seller/best-seller.component';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {FlexLayoutModule} from '@angular/flex-layout';
import {SharedModule} from './shared/shared.module';
import {FullComponent} from './layouts/full/full.component';
import {AppHeaderComponent} from './layouts/full/header/header.component';
import {AppSidebarComponent} from './layouts/full/sidebar/sidebar.component';
import {HTTP_INTERCEPTORS} from "@angular/common/http";
import {SignupComponent} from './signup/signup.component';
import {NgxUiLoaderConfig, NgxUiLoaderModule, SPINNER} from "ngx-ui-loader";
import {ForgotPasswordComponent} from './forgot-password/forgot-password.component';
import {LoginComponent} from './login/login.component';
import {TokenInterceptor} from "./services/token.interceptor";
import {AboutMeComponent} from "./about-me/about-me.component";

/**
 * Definition of ui loader:
 * simple animations that are used to keep visitors entertained while the page is still loading,
 * which helps to increase the user experience
 */
const ngUiLoaderConfig: NgxUiLoaderConfig = {
  text: "Loading...",
  textColor: "#FFFFFF",
  textPosition: "center-center",
  bgsColor: "#6a9c7a",
  fgsColor: "#6a9c7a",
  fgsType: SPINNER.squareJellyBox,
  fgsSize: 100,
  hasProgressBar: false
}

// import ngx-translate and the http loader
import {TranslateLoader, TranslateModule} from '@ngx-translate/core';
import {TranslateHttpLoader} from '@ngx-translate/http-loader';
import {HttpClient, HttpClientModule} from '@angular/common/http';

@NgModule({
  declarations: [
    AppComponent,
    HomeComponent,
    BestSellerComponent,
    FullComponent,
    AppHeaderComponent,
    AppSidebarComponent,
    SignupComponent,
    ForgotPasswordComponent,
    LoginComponent,
    AboutMeComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    BrowserAnimationsModule,
    FormsModule,
    ReactiveFormsModule,
    MaterialModule,
    FlexLayoutModule,
    SharedModule,
    HttpClientModule,
    NgxUiLoaderModule.forRoot(ngUiLoaderConfig),
    BrowserModule,
    // ngx-translate and the loader module
    HttpClientModule,
    TranslateModule.forRoot({
      loader: {
        provide: TranslateLoader,
        useFactory: HttpLoaderFactory,
        deps: [HttpClient]
      }
    })
  ],
  providers: [HttpClientModule, {provide: HTTP_INTERCEPTORS, useClass: TokenInterceptor, multi: true}],
  bootstrap: [AppComponent]
})
export class AppModule {
}

// The HttpLoaderFactory function is required for AOT (ahead of time) compilation in your project.
export function HttpLoaderFactory(http: HttpClient): TranslateHttpLoader {
  return new TranslateHttpLoader(http);
}
