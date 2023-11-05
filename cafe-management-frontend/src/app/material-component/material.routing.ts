import {Routes} from '@angular/router';
import {ManageCategoryComponent} from "./manage-category/manage-category.component";
import {RouteGuardService} from "../services/route-guard.service";
import {GlobalConstants} from "../shared/global-constants";
import {ManageProductComponent} from "./manage-product/manage-product.component";


export const MaterialRoutes: Routes = [
  {
    path: GlobalConstants.categoryPath,
    component: ManageCategoryComponent,
    canActivate: [RouteGuardService],
    data: {expectedRole: [GlobalConstants.roleAdmin]}
  },
  {
    path: GlobalConstants.productPath,
    component: ManageProductComponent,
    canActivate: [RouteGuardService],
    data: {expectedRole: [GlobalConstants.roleAdmin]}
  }
];
