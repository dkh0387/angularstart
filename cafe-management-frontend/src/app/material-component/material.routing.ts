import {Routes} from '@angular/router';
import {ManageCategoryComponent} from "./manage-category/manage-category.component";
import {RouteGuardService} from "../services/route-guard.service";
import {GlobalConstants} from "../shared/global-constants";


export const MaterialRoutes: Routes = [
  {
    path: GlobalConstants.categoryPath,
    component: ManageCategoryComponent,
    canActivate: [RouteGuardService],
    data: {expectedRole: [GlobalConstants.roleAdmin]}
  }
];
