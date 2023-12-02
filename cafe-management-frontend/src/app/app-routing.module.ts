import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {HomeComponent} from './home/home.component';
import {RouteGuardService} from "./services/route-guard.service";
import {GlobalConstants} from "./shared/global-constants";
import {AboutMeComponent} from "./about-me/about-me.component";
import {ConfirmPaymentComponent} from "./confirm-payment/confirm-payment.component";

const routes: Routes = [
  {
    path: '',
    component: HomeComponent,
    children: [
      {
        path: '',
        loadChildren:
          () => import('./material-component/material.module').then(m => m.MaterialComponentsModule),
        canActivate: [RouteGuardService],
        data: {
          expectedRole: [GlobalConstants.roleUser, GlobalConstants.roleAdmin]
        }
      },
      {
        path: GlobalConstants.dashboardPath,
        loadChildren: () => import('./dashboard/dashboard.module').then(m => m.DashboardModule),
        canActivate: [RouteGuardService],
        data: {
          expectedRole: [GlobalConstants.roleUser, GlobalConstants.roleAdmin]
        }
      }
    ]
  },
  {
    path: GlobalConstants.aboutMePath,
    component: AboutMeComponent,
    canActivate: [RouteGuardService],
    data: {expectedRole: [GlobalConstants.roleAdmin, GlobalConstants.roleUser]}
  },
  {
    path: GlobalConstants.paypalConfirmationPath + "/:" + GlobalConstants.transactionIdKey,
    component: ConfirmPaymentComponent,
    canActivate: [RouteGuardService],
    data: {expectedRole: [GlobalConstants.roleAdmin, GlobalConstants.roleUser]}
  },
  {path: '**', component: HomeComponent}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {
}
