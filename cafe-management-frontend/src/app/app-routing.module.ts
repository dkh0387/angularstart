import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {HomeComponent} from './home/home.component';
import {FullComponent} from './layouts/full/full.component';
import {RouteGuardService} from "./services/route-guard.service";
import {GlobalConstants} from "./shared/global-constants";

const routes: Routes = [
  {path: '', component: HomeComponent},
  {
    path: GlobalConstants.homePath,
    component: FullComponent,
    children: [
      {
        path: '',
        redirectTo: `/${GlobalConstants.homePath}/${GlobalConstants.dashboardPath}`,
        pathMatch: 'full',
      },
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
  {path: '**', component: HomeComponent}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {
}
